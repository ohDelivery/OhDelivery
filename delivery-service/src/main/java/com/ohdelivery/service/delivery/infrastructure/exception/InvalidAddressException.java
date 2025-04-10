package com.ohdelivery.service.delivery.infrastructure.exception;

public class InvalidAddressException extends RuntimeException {

    public InvalidAddressException(String message) {
        super(message);
    }

    public InvalidAddressException() {
        super("유효하지 않은 주소입니다!");
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
