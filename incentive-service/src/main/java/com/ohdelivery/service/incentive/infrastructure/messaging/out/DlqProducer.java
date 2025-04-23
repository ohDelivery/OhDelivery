package com.ohdelivery.service.incentive.infrastructure.messaging.out;

import java.time.Instant;
import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.infrastructure.messaging.dto.DlqMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DlqProducer {
	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void sendToDlq(String topic, UUID riderId, IncentiveType type, Exception e) {
		DlqMessage message = new DlqMessage(riderId, type, e.getMessage(), Instant.now());
		kafkaTemplate.send(topic, riderId.toString(), message);
		log.warn("📦 DLQ 전송 완료: topic={}, key={}, reason={}", topic, riderId, e.getMessage());
	}
}
