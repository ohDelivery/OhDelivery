package com.ohdelivery.service.match.matching.application.exception;

public class MatchingNotFoundException extends RuntimeException {

    public MatchingNotFoundException(String message) {
        super(message);
    }

    public MatchingNotFoundException() {
        super("존재하지 않는 매칭입니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
} 