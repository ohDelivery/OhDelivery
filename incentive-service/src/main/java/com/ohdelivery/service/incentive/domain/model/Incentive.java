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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_incentive")
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
	private String criteria;

	@Column(nullable = false)
	private Integer amount;

	@Column(nullable = false)
	private LocalDateTime paidDate;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private PaidStatus status;

}
