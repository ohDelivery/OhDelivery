package com.ohdelivery.service.consult.agent.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AgentErrorCode {
  AGENT_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 상담원입니다."),
  AGENT_ID_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 상담원입니다."),
  AGENT_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "현재 상담 가능한 상담원이 없습니다."),
  DELETED_AGENT(HttpStatus.BAD_REQUEST, "이미 삭제된 상담원입니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
