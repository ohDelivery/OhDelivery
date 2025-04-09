package com.ohdelivery.service.incentive.infrastructure.messaging;

import java.time.Duration;
import java.util.UUID;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;
import com.ohdelivery.service.incentive.application.IncentiveService;
import com.ohdelivery.service.incentive.domain.model.IncentiveType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class IncentiveStreamProcessor {

	private final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule()) // LocalDateTime 지원
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 포맷 使用
	private final IncentiveService incentiveService;

	@Bean
	public KStream<String, byte[]> kStream(StreamsBuilder builder) {
		KStream<String, byte[]> stream = builder.stream("delivery-record-create");

		KStream<String, DeliveryIncentiveDto> parsedStream = stream.mapValues(value -> {
			try {
				return objectMapper.readValue(value, DeliveryIncentiveDto.class);
			} catch (Exception e) {
				log.error("역직렬화 실패! 메시지: {}", new String(value), e);
				throw new RuntimeException("역직렬화 실패", e);

			}
		});

		// [1] 예상 시간 내 도착 → 건 당  인센티브
		parsedStream
			.filter((key, dto) -> {
				log.info("DTO 상태 확인 => expectedTime: {}, deliveredAt: {}, departedAt: {}",
					dto.getExpectedTime(), dto.getDeliveredAt(), dto.getDepartedAt());

				long actualSeconds = Duration.between(dto.getDepartedAt(), dto.getDeliveredAt()).getSeconds();
				return actualSeconds <= dto.getExpectedTime() * 60;
			})
			.foreach((key, dto) -> {
				log.info("건당 인센티브 저장: {}", dto.getRiderId());
				incentiveService.create(dto.getRiderId(), IncentiveType.PER_DELIVERY_TIME);
			});


		// [2] 최단 거리 누적 → 하루 30km 초과 시 거리 인센티브
		parsedStream
			.groupBy((key, dto) -> dto.getRiderId().toString(), Grouped.with(Serdes.String(), new DeliverySerde()))
			.windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(1))) //하루 단위로 윈도우 집계
			.aggregate(
				() -> 0,
				(riderId, dto, agg) -> agg + dto.getShortedDistance(),
				Materialized.with(Serdes.String(), Serdes.Integer())
			)
			.toStream()
			.filter((key, total) -> total > 30)
			.foreach((windowedKey, total) -> {
				String riderId = windowedKey.key(); // windowedKey = Windowed<String>
				log.info("누적 거리 인센티브 저장: {} (총 {}km)", riderId, total);
				incentiveService.create(UUID.fromString(riderId), IncentiveType.DAILY_DISTANCE);
			});

		return stream;
	}

	public static class DeliverySerde implements Serde<DeliveryIncentiveDto> {
		private final ObjectMapper mapper = new ObjectMapper()
			.registerModule(new JavaTimeModule())
			.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // ISO-8601 유지


		@Override
		public Serializer<DeliveryIncentiveDto> serializer() {
			return (topic, data) -> {
				try {
					return mapper.writeValueAsBytes(data);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		}

		@Override
		public Deserializer<DeliveryIncentiveDto> deserializer() {
			return (topic, bytes) -> {
				try {
					return mapper.readValue(bytes, DeliveryIncentiveDto.class);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			};
		}
	}
}
