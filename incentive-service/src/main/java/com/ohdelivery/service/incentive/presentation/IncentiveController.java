package com.ohdelivery.service.incentive.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;
import com.ohdelivery.service.incentive.infrastructure.messaging.KafkaProducer;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incentives")
public class IncentiveController {

	private final KafkaProducer kafkaProducer;

	@PostMapping
	public ResponseEntity<String> create(@RequestBody DeliveryIncentiveDto dto) {
		kafkaProducer.deliveryRecordEvent(dto);
		return ResponseEntity.ok("Success");
	}
}
