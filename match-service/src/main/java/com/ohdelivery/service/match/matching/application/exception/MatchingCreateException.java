package com.ohdelivery.service.match.matching.application.exception;

public class MatchingCreateException extends RuntimeException {

  public MatchingCreateException(String message) {
    super(message);
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
