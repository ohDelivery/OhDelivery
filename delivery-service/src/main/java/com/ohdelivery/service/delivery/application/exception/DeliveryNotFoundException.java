package com.ohdelivery.service.delivery.application.exception;

public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException(String message) {
        super(message);
    }

    public DeliveryNotFoundException() {
        super("존재 않는 배달 입니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
