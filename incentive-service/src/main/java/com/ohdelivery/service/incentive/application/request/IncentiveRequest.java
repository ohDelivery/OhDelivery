package com.ohdelivery.service.incentive.application.request;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.domain.model.PaidStatus;

import lombok.Getter;

public class IncentiveRequest {
	@Getter
	public static class updateIncentiveRequest{
		private UUID userId;
		private IncentiveType incentiveType;
		private int amount;
		private LocalDateTime paidDate;
		private PaidStatus status;

	}
}
