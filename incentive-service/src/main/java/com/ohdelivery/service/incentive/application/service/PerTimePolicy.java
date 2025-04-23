package com.ohdelivery.service.incentive.application.service;

import java.time.Duration;
import java.util.UUID;

import org.apache.kafka.streams.kstream.KStream;
import org.springframework.stereotype.Component;

import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;
import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.infrastructure.messaging.out.DlqProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class PerTimePolicy implements IncentivePolicy {
	private final IncentiveService incentiveService;
	private final DlqProducer dlqProducer;

	@Override
	public boolean validate(DeliveryIncentiveDto dto) {
		return dto.getExpectedTime() != null &&
			dto.getDepartedAt() != null &&
			dto.getDeliveredAt() != null;
	}

	@Override
	public void process(KStream<String, DeliveryIncentiveDto> stream) {
		stream
			.filter((key, dto) -> {
				long actualSeconds = Duration.between(dto.getDepartedAt(), dto.getDeliveredAt()).getSeconds();
				log.info("⏱ riderId={}, actual={}, expected={}", dto.getRiderId(), actualSeconds, dto.getExpectedTime() * 60);
				return actualSeconds <= dto.getExpectedTime() * 60;
			})
			.foreach((key, dto) -> {
				int maxRetry = 3; //재시도 3회
				int attempt = 0;
				boolean success = false;

				processIncentiveRetry(key, dto, attempt, maxRetry, success);
			});
	}

	private void processIncentiveRetry(String key, DeliveryIncentiveDto dto, int attempt, int maxRetry, boolean success) {
		while (attempt < maxRetry && !success) {
			try {
				incentiveService.create(dto.getRiderId(), IncentiveType.PER_DELIVERY_TIME);
				log.info("[건당 인센티브 지급 완료] riderId: {}, attempt: {}", dto.getRiderId(), attempt + 1);
				success = true;
			} catch (Exception e) {
				attempt++;
				log.warn("❗ 인센티브 지급 실패: riderId={}, attempt={}, error={}", dto.getRiderId(), attempt, e.getMessage());

				if (attempt == maxRetry) {
					sendToDLQ(key, e);
				} else {
					try {
						Thread.sleep(1000); //재시도 간격
					} catch (InterruptedException ignored) {}
				}
			}
		}
	}

	private void sendToDLQ(String key, Exception e) {
		try {
			UUID riderUUID = UUID.fromString(key);
			dlqProducer.sendToDlq("incentive-dlq", riderUUID, IncentiveType.PER_DELIVERY_TIME, e);
		} catch (IllegalArgumentException ex) {
			log.error("❌ key 형식이 UUID가 아님, DLQ 전송 불가: {}", key);
		}
	}
}
