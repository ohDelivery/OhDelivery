package com.ohdelivery.service.consult.matching.domain.strategy;

import com.ohdelivery.service.consult.agent.application.exception.AgentErrorCode;
import com.ohdelivery.service.consult.agent.application.exception.AgentException;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import com.ohdelivery.service.consult.matching.domain.repository.RedisAgentRepository;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("roundRobin")
@RequiredArgsConstructor
public class RoundRobinStrategy implements AgentMatchingStrategy {

  private final RedisAgentRepository redisAgentRepository;

  @Override
  public Long selectAgent() {
    // 현재 상담 가능한 상태인 상담원 조회
    List<String> availableAgents = getAvailableAgents();

    // 라운드 로빈 인덱스 조회 후 업데이트
    int index = getRRIndex(availableAgents.size());

    // 인덱스에 해당하는 상담원 ID 조회 후 상태 업데이트
    String agentId = availableAgents.get(index);
    redisAgentRepository.updateStatus(agentId, AgentStatus.BUSY.toString());

    // 인덱스에 해당하는 상담원 ID 반환
    return Long.valueOf(agentId);
  }

  private List<String> getAvailableAgents() {
    Set<String> agents = redisAgentRepository.findByStatus(AgentStatus.AVAILABLE.toString());
    if (agents == null || agents.isEmpty()) {
      throw new AgentException(AgentErrorCode.AGENT_NOT_AVAILABLE);
    }

    // list로 변환하여 정렬 후 리턴
    List<String> sortedAgents = new ArrayList<>(agents);
    Collections.sort(sortedAgents);
    return sortedAgents;
  }

  private int getRRIndex(int listSize) {
    int index = redisAgentRepository.getRRIndex()
        .map(Integer::parseInt)
        .map(i -> (i + 1) % listSize)
        .orElse(0);
    redisAgentRepository.updateRRIndex(String.valueOf(index));

    return index;
  }
}
