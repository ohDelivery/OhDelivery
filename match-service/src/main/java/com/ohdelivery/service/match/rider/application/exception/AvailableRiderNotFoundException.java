package com.ohdelivery.service.match.rider.application.exception;

public class AvailableRiderNotFoundException extends RuntimeException {

  public AvailableRiderNotFoundException(String message) {
    super(message);
  }

  public AvailableRiderNotFoundException() {
    super("현재 운행 가능한 라이더가 없습니다.");
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
} 