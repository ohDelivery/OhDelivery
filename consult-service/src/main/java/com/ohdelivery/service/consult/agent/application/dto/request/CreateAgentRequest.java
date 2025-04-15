package com.ohdelivery.service.consult.agent.application.dto.request;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import lombok.Getter;

@Getter
public class CreateAgentRequest {

  private Long agentId;

  public Agent toEntity() {
    return Agent.builder()
        .agentId(this.agentId)
        .build();
  }
}
