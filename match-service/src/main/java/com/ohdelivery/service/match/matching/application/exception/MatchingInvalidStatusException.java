package com.ohdelivery.service.match.matching.application.exception;

public class MatchingInvalidStatusException extends RuntimeException {

    public MatchingInvalidStatusException(String message) {
        super(message);
    }

    public MatchingInvalidStatusException() {
        super("유효하지 않은 매칭 상태입니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
} 