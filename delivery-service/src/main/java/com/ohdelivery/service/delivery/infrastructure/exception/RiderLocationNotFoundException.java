package com.ohdelivery.service.delivery.infrastructure.exception;

public class RiderLocationNotFoundException extends RuntimeException {

    public RiderLocationNotFoundException(String message) {
        super(message);
    }

    public RiderLocationNotFoundException() {
        super("라이더 위치를 찾을 수 없습니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
