package com.ohdelivery.service.incentive.infrastructure.messaging.config;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;

public class DeliverySerde implements Serde<DeliveryIncentiveDto> {
	private final ObjectMapper mapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
