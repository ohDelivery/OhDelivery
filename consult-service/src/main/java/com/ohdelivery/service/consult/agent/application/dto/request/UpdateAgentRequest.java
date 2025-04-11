package com.ohdelivery.service.consult.agent.application.dto.request;

import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import lombok.Getter;

@Getter
public class UpdateAgentRequest {

  private AgentStatus status;
}
