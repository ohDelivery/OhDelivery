package com.ohdelivery.service.incentive.infrastructure.messaging.in;

import java.time.Duration;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;
import com.ohdelivery.service.incentive.application.service.IncentiveService;
import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.infrastructure.messaging.config.DailyIncentiveTransformer;
import com.ohdelivery.service.incentive.infrastructure.messaging.config.DeliverySerde;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncentiveStreamProcessor {

	private final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	private final IncentiveService incentiveService;

	@Bean
	public KStream<String, byte[]> kStream(StreamsBuilder builder) {
		builder.addStateStore(
			Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore("daily-incentive-store"),
				Serdes.String(),
				Serdes.Boolean()
			)
		);

		KStream<String, byte[]> stream = builder.stream("delivery-record-create");
		KStream<String, DeliveryIncentiveDto> parsedStream = stream
			.mapValues(this::deserialize)
			.filter((key, dto) -> isValidDto(dto));

		processPerDeliveryIncentive(parsedStream);
		processDailyDistanceIncentive(parsedStream);

		return stream;
	}

	private DeliveryIncentiveDto deserialize(byte[] value) {
		try {
			return objectMapper.readValue(value, DeliveryIncentiveDto.class);
		} catch (Exception e) {
			log.error("역직렬화 실패! 메시지: {}", new String(value), e);
			return null;
		}
	}

	private boolean isValidDto(DeliveryIncentiveDto dto) {
		if (dto == null) return false;
		if (dto.getExpectedTime() == null || dto.getShortedDistance() == null ||
			dto.getRiderId() == null || dto.getDepartedAt() == null || dto.getDeliveredAt() == null) {
			log.warn("DTO 필드 누락 => dto: {}", dto);
			return false;
		}
		return true;
	}

	private void processPerDeliveryIncentive(KStream<String, DeliveryIncentiveDto> stream) {
		stream
			.filter((key, dto) -> {
				long actualSeconds = Duration.between(dto.getDepartedAt(), dto.getDeliveredAt()).getSeconds();
				log.info("DTO 상태 => expectedTime: {}, actualSeconds: {}", dto.getExpectedTime(), actualSeconds);
				return actualSeconds <= dto.getExpectedTime() * 60;
			})
			.foreach((key, dto) -> {
				log.info("건당 인센티브 지급 => {}", dto.getRiderId());
				incentiveService.create(dto.getRiderId(), IncentiveType.PER_DELIVERY_TIME);
			});
	}

	private void processDailyDistanceIncentive(KStream<String, DeliveryIncentiveDto> stream) {
		stream
			.groupBy((key, dto) -> dto.getRiderId().toString(), Grouped.with(Serdes.String(), new DeliverySerde()))
			.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(1)).advanceBy(Duration.ofDays(1)))
			.aggregate(
				() -> 0,
				(riderId, dto, agg) -> agg + dto.getShortedDistance(),
				Materialized.with(Serdes.String(), Serdes.Integer())
			)
			.toStream()
			.filter((windowedKey, total) -> total >= 30)
			.transform(() -> new DailyIncentiveTransformer(incentiveService), "daily-incentive-store");
	}
}
