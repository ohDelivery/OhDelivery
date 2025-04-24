package com.ohdelivery.service.incentive.infrastructure.messaging.in;

import java.util.List;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;
import com.ohdelivery.service.incentive.application.service.IncentivePolicy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Component
@RequiredArgsConstructor
public class IncentiveStreamProcessor {

	private final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	private final List<IncentivePolicy> policies;

	@Bean
	public KStream<String, byte[]> kStream(StreamsBuilder builder) {
		//state store 등록
		builder.addStateStore(
			Stores.keyValueStoreBuilder(
				Stores.persistentKeyValueStore("daily-incentive-store"),
				Serdes.String(),
				Serdes.Boolean()
			)
		);
		//topic consumer
		KStream<String, byte[]> stream = builder.stream("delivery-record-create");


		//DeliveryIncentiveDto 로 파싱
		KStream<String, DeliveryIncentiveDto> parsedStream = stream
			.mapValues(this::deserialize)
			.filter((key, dto) -> isValidDto(dto))
			.selectKey((ignoredKey, dto) -> dto.getRiderId().toString()); // key를 riderId로 지정


		parsedStream.peek((key, dto) -> log.info("📦 Parsed DTO: key={}, dto={}", key, dto));


		log.info("policy size"+policies.size());
		//인센티브 지급 정책에 따라 지급
		for (IncentivePolicy policy : policies) {

			policy.process(parsedStream);
		}

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
		if (dto.getRiderId() == null) return false;
		return true;
	}
}