package com.ohdelivery.service.consult.matching.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AgentMatchingErrorCode {
  AGENT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "현재 상담 가능한 상담원이 없습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
