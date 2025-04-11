package com.ohdelivery.service.incentive.infrastructure.messaging.config;

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

import com.ohdelivery.service.incentive.application.service.IncentiveService;
import com.ohdelivery.service.incentive.domain.model.IncentiveType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DailyIncentiveTransformer implements
	Transformer<Windowed<String>, Integer, KeyValue<String, Void>> {

	private final IncentiveService incentiveService;
	private KeyValueStore<String, Boolean> stateStore; //라이더키_날짜, 지급 여부

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
		String stateKey = riderId + "_" + date; //키: 라이더ID_날짜

		if (Boolean.TRUE.equals(stateStore.get(stateKey))) { //하루 한번만 지급
			log.info("[중복 방지] 이미 지급된 인센티브: {}, 날짜: {}", riderId, date);
			return null;
		}

		log.info("[인센티브 지급] riderId={}, date={}, 총거리={}km", riderId, date, totalDistance);
		incentiveService.create(UUID.fromString(riderId), IncentiveType.DAILY_DISTANCE);
		stateStore.put(stateKey, true);

		return null;
	}


	@Override
	public void close() {}
}
