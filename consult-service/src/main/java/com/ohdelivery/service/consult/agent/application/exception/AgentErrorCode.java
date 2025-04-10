package com.ohdelivery.service.consult.agent.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AgentErrorCode {
  AGENT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상담원입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
