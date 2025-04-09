package com.ohdelivery.service.delivery.application.exception;

public class DeliveryInvalidStatusException extends RuntimeException {

    public DeliveryInvalidStatusException(String message) {
        super(message);
    }

    public DeliveryInvalidStatusException() {
        super("요청을 수행하기에 유효한 상태가 아닙니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
