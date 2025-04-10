package com.ohdelivery.service.consult.agent.application.service;

import com.ohdelivery.service.consult.agent.application.dto.request.CreateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.request.UpdateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.response.AgentResponse;
import com.ohdelivery.service.consult.agent.application.exception.AgentErrorCode;
import com.ohdelivery.service.consult.agent.application.exception.AgentException;
import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import com.ohdelivery.service.consult.agent.domain.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgentService {

  private final AgentRepository agentRepository;

  // todo: role check

  @Transactional
  public AgentResponse createAgent(CreateAgentRequest request) {
    Agent agent = request.toEntity(AgentStatus.OFFLINE);
    agentRepository.save(agent);
    return AgentResponse.toDto(agent);
  }

  @Transactional
  public AgentResponse updateAgent(Long agentId, UpdateAgentRequest request) {
    Agent agent = findAgent(agentId);
    agent.updateStatus(request.getStatus());
    return AgentResponse.toDto(agent);
  }

  private Agent findAgent(Long agentId) {
    return agentRepository.findByAgentId(agentId)
        .orElseThrow(() -> new AgentException(AgentErrorCode.AGENT_ID_NOT_FOUND));
  }
}
