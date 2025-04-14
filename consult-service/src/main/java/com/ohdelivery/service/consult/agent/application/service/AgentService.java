package com.ohdelivery.service.consult.agent.application.service;

import com.ohdelivery.service.consult.agent.application.dto.request.CreateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.request.UpdateAgentRequest;
import com.ohdelivery.service.consult.agent.application.dto.response.AgentResponse;
import com.ohdelivery.service.consult.agent.application.exception.AgentErrorCode;
import com.ohdelivery.service.consult.agent.application.exception.AgentException;
import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import com.ohdelivery.service.consult.agent.domain.repository.AgentRepository;
import com.ohdelivery.service.consult.matching.domain.repository.RedisAgentRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AgentService {

  private final AgentRepository agentRepository;
  private final RedisAgentRepository redisAgentRepository;

  // todo: role check

  @Transactional
  public AgentResponse createAgent(CreateAgentRequest request) {
    checkExistingAgent(request.getAgentId());
    Agent agent = request.toEntity();
    agentRepository.save(agent);
    redisAgentRepository.save(request.getAgentId().toString());
    return AgentResponse.toDto(agent, AgentStatus.OFFLINE);
  }

  @Transactional
  public AgentResponse updateAgent(Long agentId, UpdateAgentRequest request) {
    Agent agent = findAgent(agentId);
    redisAgentRepository.updateStatus(agentId.toString(), request.getStatus().toString());
    return AgentResponse.toDto(agent, request.getStatus());
  }

  @Transactional(readOnly = true)
  public AgentResponse getAgent(Long agentId) {
    Agent agent = findAgent(agentId);
    String status = redisAgentRepository.getStatus(agentId.toString());
    return AgentResponse.toDto(agent, AgentStatus.valueOf(status));
  }

  @Transactional
  public void deleteAgent(Long agentId) {
    Agent agent = findAgent(agentId);

    // todo: deletedBy -> 로그인한 유저 id로 변경
    agent.delete(LocalDateTime.now(), agentId.toString());
    redisAgentRepository.delete(agentId.toString());
  }

  private void checkExistingAgent(Long agentId) {
    if (agentRepository.findByAgentIdAndDeletedAtIsNull(agentId).isPresent()) {
      throw new AgentException(AgentErrorCode.AGENT_ID_ALREADY_EXISTS);
    }
  }

  private Agent findAgent(Long agentId) {
    return agentRepository.findByAgentIdAndDeletedAtIsNull(agentId)
        .orElseThrow(() -> new AgentException(AgentErrorCode.AGENT_ID_NOT_FOUND));
  }
}
