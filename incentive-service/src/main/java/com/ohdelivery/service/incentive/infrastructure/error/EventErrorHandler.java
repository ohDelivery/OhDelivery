package com.ohdelivery.service.incentive.infrastructure.error;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventErrorHandler {

	private final KafkaTemplate<String, Object> kafkaTemplate;

	public void handleEventError(
		Exception exception,
		Object message,
		String errorTopic) {

		log.error("이벤트 처리 중 오류 발생: {}", exception.getMessage(), exception);

		// 메시지에 에러 정보를 주입할 수 없다면 그대로 보냄
		kafkaTemplate.send(errorTopic, message);
	}
}
