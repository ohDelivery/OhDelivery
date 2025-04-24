package com.ohdelivery.service.incentive.application.service;

import org.apache.kafka.streams.kstream.KStream;

import com.ohdelivery.common.kafka.dto.DeliveryIncentiveDto;

public interface IncentivePolicy {
	boolean validate(DeliveryIncentiveDto dto);
	void process(KStream<String, DeliveryIncentiveDto> stream);
}
