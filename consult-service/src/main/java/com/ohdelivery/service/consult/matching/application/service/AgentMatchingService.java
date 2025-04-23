package com.ohdelivery.service.consult.matching.application.service;

import com.ohdelivery.service.consult.matching.application.dto.AgentMatchingEvent;
import com.ohdelivery.service.consult.matching.application.dto.CreateMatchingRequest;
import com.ohdelivery.service.consult.matching.domain.model.AgentMatching;
import com.ohdelivery.service.consult.matching.domain.repository.AgentMatchingRepository;
import com.ohdelivery.service.consult.matching.domain.strategy.AgentMatchingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgentMatchingService {

  private final AgentMatchingRepository agentMatchingRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final AgentMatchingStrategy strategy;

  public AgentMatchingService(
      AgentMatchingRepository agentMatchingRepository,
      ApplicationEventPublisher eventPublisher,
      @Qualifier("leastBusy") AgentMatchingStrategy strategy) {
    this.agentMatchingRepository = agentMatchingRepository;
    this.eventPublisher = eventPublisher;
    this.strategy = strategy;
  }

  @Transactional
  public void createMatching(CreateMatchingRequest request) {
    // 상담원 선택 후 상태 업데이트
    Long agentId = strategy.selectAgent();

    // 상담원 매칭 이벤트 발행
    AgentMatchingEvent event = new AgentMatchingEvent(this, request.getRiderId(), agentId);
    eventPublisher.publishEvent(event);

    // 상담원 매칭 정보 저장
    AgentMatching matching = request.toEntity(agentId);
    agentMatchingRepository.save(matching);
  }
}
