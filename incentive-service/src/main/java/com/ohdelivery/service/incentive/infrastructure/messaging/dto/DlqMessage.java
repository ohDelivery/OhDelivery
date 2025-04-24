package com.ohdelivery.service.incentive.infrastructure.messaging.dto;

import java.time.Instant;
import java.util.UUID;

import com.ohdelivery.service.incentive.domain.model.IncentiveType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DlqMessage {
	private UUID riderId;
	private IncentiveType incentiveType;
	private String errorMessage;
	private Instant failedAt;
}
