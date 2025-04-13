package com.ohdelivery.service.consult.agent.application.service;

import com.ohdelivery.service.consult.agent.application.dto.event.AgentMatchingEvent;
import com.ohdelivery.service.consult.agent.application.dto.request.CreateMatchingRequest;
import com.ohdelivery.service.consult.agent.application.exception.AgentErrorCode;
import com.ohdelivery.service.consult.agent.application.exception.AgentException;
import com.ohdelivery.service.consult.agent.domain.model.AgentMatching;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import com.ohdelivery.service.consult.agent.domain.repository.AgentMatchingRepository;
import com.ohdelivery.service.consult.agent.domain.repository.RedisAgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchingService {

  private final AgentMatchingRepository agentMatchingRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final RedisAgentRepository redisAgentRepository;

  @Transactional
  public void createMatching(CreateMatchingRequest request) {
    // 상담 가능한 상담원 중 라운드 로빈 방식으로 선택
    Long agentId = selectAgent();
    redisAgentRepository.updateStatus(agentId.toString(), AgentStatus.BUSY.toString());

    // 상담원 매칭 이벤트 발행
    AgentMatchingEvent event = new AgentMatchingEvent(this, request.getRiderId(), agentId);
    eventPublisher.publishEvent(event);

    // 상담원 매칭 정보 저장
    AgentMatching matching = request.toEntity(agentId);
    agentMatchingRepository.save(matching);
  }

  private Long selectAgent() {
    return redisAgentRepository.selectAgent()
        .orElseThrow(() -> new AgentException(AgentErrorCode.AGENT_NOT_AVAILABLE));
  }
}
