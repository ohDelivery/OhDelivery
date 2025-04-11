package com.ohdelivery.service.consult.agent.application.exception;

import com.ohdelivery.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AgentExceptionHandler {

  @ExceptionHandler(AgentException.class)
  public ResponseEntity<ApiResponse<String>> handlerAgentExceptionException(AgentException e) {
    AgentErrorCode code = e.getErrorCode();
    return ResponseEntity
        .status(code.getHttpStatus())
        .body(ApiResponse.fail(code.name(), code.getMessage()));
  }
}
