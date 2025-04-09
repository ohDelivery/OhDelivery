package com.ohdelivery.service.match.alarm.application.exception;

import lombok.Getter;

@Getter
public class AlarmException extends RuntimeException {

  private final AlarmErrorCode errorCode;

  public AlarmException(AlarmErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
