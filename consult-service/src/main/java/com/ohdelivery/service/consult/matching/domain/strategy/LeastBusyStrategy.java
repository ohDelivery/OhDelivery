package com.ohdelivery.service.consult.matching.domain.strategy;

import com.ohdelivery.service.consult.agent.application.exception.AgentErrorCode;
import com.ohdelivery.service.consult.agent.application.exception.AgentException;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import com.ohdelivery.service.consult.matching.domain.repository.RedisAgentRepository;
import java.util.Comparator;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("leastBusy")
@RequiredArgsConstructor
public class LeastBusyStrategy implements AgentMatchingStrategy {

  private final RedisAgentRepository redisAgentRepository;

  @Override
  public Long selectAgent() {
    // 현재 상담 가능한 상태인 상담원 조회
    Set<String> availableAgents = getAvailableAgents();

    // 가장 적은 busy count를 가진 상담원 ID 조회
    String agentId = getMinBusyAgentId(availableAgents);

    // 상담원 상태 업데이트 후 busy count 증가
    redisAgentRepository.updateStatus(agentId, AgentStatus.BUSY.toString());
    redisAgentRepository.increaseBusyCount(agentId);

    return Long.valueOf(agentId);
  }

  private Set<String> getAvailableAgents() {
    Set<String> agents = redisAgentRepository.findByStatus(AgentStatus.AVAILABLE.toString());
    if (agents == null || agents.isEmpty()) {
      throw new AgentException(AgentErrorCode.AGENT_NOT_AVAILABLE);
    }
    return agents;
  }

  private String getMinBusyAgentId(Set<String> agents) {
    return agents.stream()
        .min(Comparator.comparingInt(redisAgentRepository::getBusyCount))
        .orElseThrow(() -> new AgentException(AgentErrorCode.AGENT_NOT_AVAILABLE));
  }
}
