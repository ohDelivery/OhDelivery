package com.ohdelivery.service.consult.agent.application.exception;

import lombok.Getter;

@Getter
public class AgentException extends RuntimeException {

  private final AgentErrorCode errorCode;

  public AgentException(AgentErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
