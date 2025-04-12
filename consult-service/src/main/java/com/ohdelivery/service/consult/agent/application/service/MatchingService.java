package com.ohdelivery.service.consult.agent.application.service;

import com.ohdelivery.service.consult.agent.application.dto.event.AgentMatchingEvent;
import com.ohdelivery.service.consult.agent.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.consult.agent.application.exception.AgentErrorCode;
import com.ohdelivery.service.consult.agent.application.exception.AgentException;
import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.model.AgentMatching;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import com.ohdelivery.service.consult.agent.domain.repository.AgentMatchingRepository;
import com.ohdelivery.service.consult.agent.domain.repository.AgentRepository;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final AgentRepository agentRepository;
  private final AgentMatchingRepository agentMatchingRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final AtomicInteger counter = new AtomicInteger(0);

  @Transactional
  public void createMatching(CreateMatchingRequest request) {
    // 상담 가능한 상담원 중 라운드 로빈 방식으로 선택
    // todo: redis에서 상태 관리
    List<Agent> agents = agentRepository.findByStatus(AgentStatus.AVAILABLE);
    Long agentId = selectAgent(agents);

    // 상담원 매칭 이벤트 발행
    AgentMatchingEvent event = new AgentMatchingEvent(this, request.getRiderId(), agentId);
    eventPublisher.publishEvent(event);

    // 상담원 매칭 정보 저장
    AgentMatching matching = request.toEntity(agentId);
    agentMatchingRepository.save(matching);
  }

  private Long selectAgent(List<Agent> agents) {
    if (agents.isEmpty()) {
      throw new AgentException(AgentErrorCode.AGENT_NOT_AVAILABLE);
    }

    // 라운드 로빈 방식으로 상담원 선택
    int index = counter.getAndIncrement() % agents.size();
    Agent selectedAgent = agents.get(index);
    selectedAgent.updateStatus(AgentStatus.BUSY);

    return selectedAgent.getAgentId();
  }
}
