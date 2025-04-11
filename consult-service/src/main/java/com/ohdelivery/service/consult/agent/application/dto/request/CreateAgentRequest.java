package com.ohdelivery.service.consult.agent.application.dto.request;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import lombok.Getter;

@Getter
public class CreateAgentRequest {

  private Long agentId;

  public Agent toEntity(AgentStatus agentStatus) {
    return Agent.builder()
        .agentId(this.agentId)
        .status(agentStatus)
        .build();
  }
}
