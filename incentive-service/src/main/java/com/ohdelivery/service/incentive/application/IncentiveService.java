package com.ohdelivery.service.incentive.application;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ohdelivery.service.incentive.domain.model.Incentive;
import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.domain.repository.IncentiveRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IncentiveService {

	private final IncentiveRepository incentiveRepository;

	public void create(UUID riderId, IncentiveType incentiveType) {
		int amount;
		if (incentiveType.equals(IncentiveType.DAILY_DISTANCE)) {
			amount = 1000;
		} else {
			amount = 5000;
		}

		Incentive incentive = Incentive.create(riderId, incentiveType, amount);
		incentiveRepository.save(incentive);
		log.info("Incentive created: {}", incentive);
	}
}
