package com.ohdelivery.service.consult.matching.application.dto;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class AgentMatchingEvent extends ApplicationEvent {

  private Long riderId;
  private Long agentId;

  public AgentMatchingEvent(Object source, Long riderId, Long agentId) {
    super(source);
    this.riderId = riderId;
    this.agentId = agentId;
  }
}
