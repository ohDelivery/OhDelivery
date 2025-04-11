package com.ohdelivery.service.incentive.application.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ohdelivery.service.incentive.application.exception.IncentiveException;
import com.ohdelivery.service.incentive.application.request.IncentiveRequest;
import com.ohdelivery.service.incentive.application.response.IncentiveResponse;
import com.ohdelivery.service.incentive.domain.model.Incentive;
import com.ohdelivery.service.incentive.domain.model.IncentiveType;
import com.ohdelivery.service.incentive.domain.repository.IncentiveRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional //트랜잭션 설정
public class IncentiveService {

	private final IncentiveRepository incentiveRepository;

	public void create(UUID riderId, IncentiveType incentiveType) {
		int amount;
		if (incentiveType.equals(IncentiveType.DAILY_DISTANCE)) {
			amount = 5000;
		} else {
			amount = 1000;
		}

		Incentive incentive = Incentive.create(riderId, incentiveType, amount);
		incentiveRepository.save(incentive);
		log.info("Incentive created: {}", incentive);
	}

	@Transactional(readOnly = true)
	public IncentiveResponse.GetIncentiveResponse getIncentive(UUID incentiveId) {
		Incentive incentive = getIncentive1(incentiveId);
		return IncentiveResponse.from(incentive);
	}

	private Incentive getIncentive1(UUID incentiveId) {
		Incentive incentive = incentiveRepository.getIncentiveById(incentiveId)
			.orElseThrow(IncentiveException.IncentiveNotFoundException::new);
		return incentive;
	}

	public void updateIncentive(UUID incentiveId, IncentiveRequest.updateIncentiveRequest request) {
		Incentive incentive = getIncentive1(incentiveId);
		incentive.update(request);
	}

	public IncentiveResponse.DeleteIncentiveResponse deleteIncentive(UUID incentiveId) {
		Incentive incentive = getIncentive1(incentiveId);
		incentiveRepository.delete(incentive);
		return IncentiveResponse.DeleteIncentiveResponse.builder().deletedIncentiveId(incentive.getId()).build();
	}
}
