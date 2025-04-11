package com.ohdelivery.service.incentive.application.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ohdelivery.service.incentive.domain.model.Incentive;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class IncentiveResponse {
	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class GetIncentiveResponse {
		private UUID userId;
		private String incentiveType;
		private int amount;
		private LocalDateTime paidDate;
		private String status;

	}
	public static GetIncentiveResponse from(Incentive incentive) {
		return GetIncentiveResponse.builder()
			.userId(incentive.getUserId())
			.incentiveType(incentive.getIncentiveType().name())
			.amount(incentive.getAmount())
			.paidDate(incentive.getPaidDate())
			.status(incentive.getStatus().name())
			.build();
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PRIVATE)
	@Builder
	public static class DeleteIncentiveResponse {
		private UUID deletedIncentiveId;

	}
}
