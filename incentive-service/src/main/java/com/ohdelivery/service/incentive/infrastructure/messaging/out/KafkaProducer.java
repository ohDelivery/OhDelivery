package com.ohdelivery.service.incentive.infrastructure.messaging.out;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {
	private static final String TOPIC = "delivery-record-create";

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void deliveryRecordEvent(DeliveryIncentiveDto incentiveDto) {
		log.info("배달기록 생성!!!!!!");
		kafkaTemplate.send(TOPIC, incentiveDto);
	}
}