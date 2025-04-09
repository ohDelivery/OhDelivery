package com.ohdelivery.service.match.rider.application.exception;

public class RiderNotFoundException extends RuntimeException {

    public RiderNotFoundException(String message) {
        super(message);
    }

    public RiderNotFoundException() {
        super("존재하지 않는 라이더입니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
} 