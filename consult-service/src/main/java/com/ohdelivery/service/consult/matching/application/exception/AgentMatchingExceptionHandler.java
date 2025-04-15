package com.ohdelivery.service.consult.matching.application.exception;

import com.ohdelivery.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AgentMatchingExceptionHandler {

  @ExceptionHandler(AgentMatchingException.class)
  public ResponseEntity<ApiResponse<String>> handlerAgentMatchingExceptionException(
      AgentMatchingException e) {
    AgentMatchingErrorCode code = e.getErrorCode();
    return ResponseEntity
        .status(code.getHttpStatus())
        .body(ApiResponse.fail(code.name(), code.getMessage()));
  }
}
