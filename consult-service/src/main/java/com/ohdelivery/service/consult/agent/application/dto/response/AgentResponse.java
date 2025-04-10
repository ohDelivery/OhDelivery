package com.ohdelivery.service.consult.agent.application.dto.response;

import com.ohdelivery.service.consult.agent.domain.model.Agent;
import com.ohdelivery.service.consult.agent.domain.model.AgentStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponse {

  private UUID id;
  private Long agentId;
  private AgentStatus status;

  public static AgentResponse toDto(Agent agent) {
    return AgentResponse.builder()
        .id(agent.getId())
        .agentId(agent.getAgentId())
        .status(agent.getStatus())
        .build();
  }
}
