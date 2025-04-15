package com.ohdelivery.service.delivery.application.exception;

import com.ohdelivery.common.response.ApiResponse;
import com.ohdelivery.service.delivery.infrastructure.exception.InvalidAddressException;
import com.ohdelivery.service.delivery.infrastructure.exception.RiderLocationNotFoundException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Hidden
@RestControllerAdvice
public class DeliveryExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = DeliveryNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> notFoundException(DeliveryNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiResponse.fail(HttpStatus.NOT_FOUND, e.getMessage())
        );
    }

    @ExceptionHandler(value = DeliveryInvalidStatusException.class)
    public ResponseEntity<ApiResponse<String>> invalidStatusException(
        DeliveryInvalidStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(value = InvalidAddressException.class)
    public ResponseEntity<ApiResponse<String>> invalidAddressException(
        InvalidAddressException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(value = RiderLocationNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> riderNotFoundException(
        RiderLocationNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiResponse.fail(HttpStatus.NOT_FOUND, e.getMessage())
        );
    }
}
