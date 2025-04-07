package com.ohdelivery.service.incentive.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum IncentiveType {
	DAILY_DISTANCE("누적 거리 기반"),
	PER_DELIVERY_TIME("예상 소요 시간 내 도착");

	private final String value;

}
