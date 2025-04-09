package com.ohdelivery.service.match.rider.application.exception;

public class RiderInvalidStatusException extends RuntimeException {

    public RiderInvalidStatusException(String message) {
        super(message);
    }

    public RiderInvalidStatusException() {
        super("유효하지 않은 라이더 상태입니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
} 