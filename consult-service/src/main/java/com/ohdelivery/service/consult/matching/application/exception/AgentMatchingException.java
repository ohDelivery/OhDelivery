package com.ohdelivery.service.consult.matching.application.exception;

import lombok.Getter;

@Getter
public class AgentMatchingException extends RuntimeException {

  private final AgentMatchingErrorCode errorCode;

  public AgentMatchingException(AgentMatchingErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
