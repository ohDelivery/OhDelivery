package com.ohdelivery.service.incentive.application;

import org.springframework.stereotype.Service;

import com.ohdelivery.service.incentive.domain.repository.IncentiveRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncentiveService {

	private final IncentiveRepository incentiveRepository;


}
