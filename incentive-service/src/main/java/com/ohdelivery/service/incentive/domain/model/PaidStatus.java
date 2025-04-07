package com.ohdelivery.service.incentive.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PaidStatus {
	PAID("지급 완료"),
	NOT_PAID("미지급");

	private final String value;
}
