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
				return actualSeconds <= dto.getExpectedTime()*60;
			})
			.foreach((key, dto) -> {
				try {
					log.info("[건당 인센티브 지급] riderId: {}", dto.getRiderId());
					incentiveService.create(dto.getRiderId(), IncentiveType.PER_DELIVERY_TIME);
				} catch (Exception e) {
					try {
						UUID riderUUID = UUID.fromString(key);
						dlqProducer.sendToDlq("incentive-dlq", riderUUID, IncentiveType.PER_DELIVERY_TIME, e);
					} catch (IllegalArgumentException ex) {
						log.error("❌ key 형식이 UUID가 아님, DLQ 전송 불가: {}", key);
					}
				}
			});
	}
}
