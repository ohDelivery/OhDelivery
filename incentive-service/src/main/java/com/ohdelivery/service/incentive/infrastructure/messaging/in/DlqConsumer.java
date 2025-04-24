package com.ohdelivery.service.incentive.infrastructure.messaging.in;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ohdelivery.service.incentive.application.service.IncentiveService;
import com.ohdelivery.service.incentive.infrastructure.messaging.dto.DlqMessage;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class DlqConsumer {

	private final IncentiveService incentiveService;

	private final ObjectMapper objectMapper = new ObjectMapper()
		.registerModule(new JavaTimeModule())
		.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

	private final Counter dlqCounter = Counter.builder("dlq_message_total")
		.description("DLQ에서 수신된 메시지 총 수")
		.tag("consumer", "dlq-consumer")
		.register(Metrics.globalRegistry);


	@KafkaListener(topics = "incentive-dlq", groupId = "dlq-retry-group")
	public void handleDlq(String message) {
		try {
			DlqMessage msg = objectMapper.readValue(message, DlqMessage.class);

			//DLQ 메시지 수신 수 증가 - 메트릭 수집
			dlqCounter.increment();
		} catch (Exception e) {
			log.error("❌ DLQ 재처리 실패. 메시지 파싱 또는 처리 실패: {}", e.getMessage(), e);
		}
	}
}
