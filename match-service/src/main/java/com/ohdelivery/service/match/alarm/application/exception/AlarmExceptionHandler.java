package com.ohdelivery.service.match.alarm.application.exception;

import com.ohdelivery.common.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AlarmExceptionHandler {

  @ExceptionHandler(AlarmException.class)
  public ResponseEntity<ApiResponse<String>> handlerAlarmExceptionException(AlarmException e) {
    AlarmErrorCode code = e.getErrorCode();
    return ResponseEntity
        .status(code.getHttpStatus())
        .body(ApiResponse.fail(code.name(), code.getMessage()));
  }
}
