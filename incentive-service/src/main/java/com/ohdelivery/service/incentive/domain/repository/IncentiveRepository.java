package com.ohdelivery.service.incentive.domain.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ohdelivery.service.incentive.domain.model.Incentive;

public interface IncentiveRepository  extends JpaRepository<Incentive, UUID> {
	Optional<Incentive> getIncentiveById(UUID incentiveId);
}
