package com.ohdelivery.service.incentive.infrastructure.messaging.in;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohdelivery.service.incentive.application.service.IncentiveService;
import com.ohdelivery.service.incentive.infrastructure.messaging.dto.DlqMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DlqConsumer {

	private final IncentiveService incentiveService;

	private final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule()) // 이거 꼭 추가!
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	@KafkaListener(topics = "incentive-dlq", groupId = "dlq-retry-group")
	public void handleDlq(String message) {
		try {
			DlqMessage msg = objectMapper.readValue(message, DlqMessage.class);
			log.info("DLQ 재처리 시도: riderId={}, type={}", msg.getRiderId(), msg.getIncentiveType());
			incentiveService.create(msg.getRiderId(), msg.getIncentiveType());
			log.info("✅ DLQ 재처리 성공: riderId={}", msg.getRiderId());
		} catch (Exception e) {
			log.error("❌ DLQ 재처리 실패. 메시지 파싱 또는 처리 실패: {}", e.getMessage(), e);
		}
	}
}
