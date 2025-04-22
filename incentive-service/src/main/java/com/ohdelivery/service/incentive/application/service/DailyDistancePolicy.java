package com.ohdelivery.service.incentive.application.service;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.stereotype.Component;

import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;
import com.ohdelivery.service.incentive.infrastructure.messaging.config.DeliverySerde;
import com.ohdelivery.service.incentive.infrastructure.messaging.out.DlqProducer;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DailyDistancePolicy implements IncentivePolicy{

	private final IncentiveService incentiveService;
	private final DlqProducer dlqProducer;

	@Override
	public boolean validate(DeliveryIncentiveDto dto) {
		return dto.getShortedDistance() != null && dto.getRiderId() != null;
	}

	@Override
	public void process(KStream<String, DeliveryIncentiveDto> stream) {
		stream
			// 라이더 ID 기준으로 묶음
			.groupBy((key, dto) -> dto.getRiderId().toString(), Grouped.with(Serdes.String(), new DeliverySerde()))
			// 하루짜리 윈도우, 하루마다 하나씩 이동
			.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(1)).advanceBy(Duration.ofDays(1)))
			.aggregate(
				() -> 0, //초기값 0
				(riderId, dto, agg) -> agg + dto.getShortedDistance(), //누적 로직
				Materialized.with(Serdes.String(), Serdes.Integer())
			)
			.toStream()
			//누적거리 30km이상
			.filter((windowedKey, total) -> total >= 30)
			.transform(() -> new DailyIncentiveTransformer(incentiveService, dlqProducer), "daily-incentive-store");
	}
}
