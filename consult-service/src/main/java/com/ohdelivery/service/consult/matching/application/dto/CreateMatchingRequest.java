package com.ohdelivery.service.consult.matching.application.dto;

import com.ohdelivery.service.consult.matching.domain.model.AgentMatching;
import lombok.Getter;

@Getter
public class CreateMatchingRequest {

  private Long riderId;

  public AgentMatching toEntity(Long agentId) {
    return AgentMatching.builder()
        .agentId(agentId)
        .riderId(this.riderId)
        .build();
  }
}
