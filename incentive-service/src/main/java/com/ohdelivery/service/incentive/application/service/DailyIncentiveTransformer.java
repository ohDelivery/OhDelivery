package com.ohdelivery.service.incentive.application.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Transformer;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.processor.PunctuationType;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.infrastructure.messaging.out.DlqProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DailyIncentiveTransformer implements
	Transformer<Windowed<String>, Integer, KeyValue<String, Void>> {

	private final IncentiveService incentiveService;
	private KeyValueStore<String, Boolean> stateStore; //라이더키_날짜, 지급 여부
	private final DlqProducer dlqProducer;

	@Override
	@SuppressWarnings("unchecked")
	public void init(ProcessorContext context) {
		this.stateStore = (KeyValueStore<String, Boolean>) context.getStateStore("daily-incentive-store");
		// 하루마다 오래된 키 정리
		context.schedule(Duration.ofDays(1), PunctuationType.WALL_CLOCK_TIME, this::clearOldKeys);
	}

	private void clearOldKeys(long timestamp) {
		LocalDate targetDate = LocalDate.now().minusDays(1);
		String suffix = "_" + targetDate;

		try (KeyValueIterator<String, Boolean> iterator = stateStore.all()) {
			while (iterator.hasNext()) {
				KeyValue<String, Boolean> entry = iterator.next();
				if (entry.key.endsWith(suffix)) {
					stateStore.delete(entry.key);
					log.info("하루 지난 키 삭제: {}", entry.key);
				}
			}
		}
	}

	@Override
	public KeyValue<String, Void> transform(Windowed<String> key, Integer totalDistance) {
		String riderId = key.key();
		LocalDate date = Instant.ofEpochMilli(key.window().start())
			.atZone(ZoneId.systemDefault())
			.toLocalDate();
		String stateKey = riderId + "_" + date; //stateStore 키

		//중복 검사
		if (Boolean.TRUE.equals(stateStore.get(stateKey))) {
			log.info("[중복 방지] 이미 지급된 인센티브: {}, 날짜: {}", riderId, date);
			return null;
		}

		int maxRetry = 3;
		int attempt = 0;
		boolean success = false;

		processDailyIncentiveRetry(totalDistance, attempt, maxRetry, success, riderId, date, stateKey);

		return null;
	}

	private void processDailyIncentiveRetry(Integer totalDistance, int attempt, int maxRetry, boolean success, String riderId,
		LocalDate date, String stateKey) {
		while (attempt < maxRetry && !success) {
			try {
				log.info("[인센티브 지급 시도] riderId={}, date={}, 총거리={}km, attempt={}", riderId, date,
					totalDistance, attempt + 1);
				incentiveService.create(UUID.fromString(riderId), IncentiveType.DAILY_DISTANCE);
				stateStore.put(stateKey, true);
				log.info("✅ 인센티브 지급 성공: riderId={}, date={}", riderId, date);
				success = true;
			} catch (Exception e) {
				//재시도 로직
				attempt++;
				log.warn("❗ 인센티브 지급 실패: riderId={}, attempt={}, 이유: {}", riderId, attempt, e.getMessage());

				if (attempt == maxRetry) {
					//재시도 실패 시 dlq 로 보냄
					sendToDLQ(riderId, e);
				} else {
					try {
						Thread.sleep(1000); // 재시도 간격
					} catch (InterruptedException ie) {
						Thread.currentThread().interrupt(); // 인터럽트 복원
					}
				}
			}
		}
	}

	private void sendToDLQ(String riderId, Exception e) {
		try {
			UUID riderUUID = UUID.fromString(riderId);
			dlqProducer.sendToDlq("incentive-dlq", riderUUID, IncentiveType.DAILY_DISTANCE, e);
		} catch (IllegalArgumentException ex) {
			log.error("❌ riderId 형식이 UUID가 아님, DLQ 전송 불가: {}", riderId);
		}
	}

	@Override
	public void close() {}
}
