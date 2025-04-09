package com.ohdelivery.service.incentive.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "p_incentive", schema = "INCENTIVE_SCHEMA")
public class Incentive {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private UUID userId;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private IncentiveType incentiveType;

	@Column(nullable = false)
	private Integer amount;

	private LocalDateTime paidDate;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private PaidStatus status;

	public static Incentive create(UUID userId, IncentiveType incentiveType, Integer amount) {
		return Incentive.builder()
			.userId(userId)
			.incentiveType(incentiveType)
			.amount(amount)
			.status(PaidStatus.NOT_PAID)
			.build();
	}

}
