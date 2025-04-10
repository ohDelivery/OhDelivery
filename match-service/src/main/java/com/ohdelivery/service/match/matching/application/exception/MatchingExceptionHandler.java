package com.ohdelivery.service.match.matching.application.exception;

import com.ohdelivery.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Hidden
@RestControllerAdvice
public class MatchingExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = MatchingNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> notFoundException(HttpServletRequest req,
        MatchingNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiResponse.fail(HttpStatus.NOT_FOUND, e.getMessage())
        );
    }

    @ExceptionHandler(value = MatchingInvalidStatusException.class)
    public ResponseEntity<ApiResponse<String>> invalidStatusException(HttpServletRequest req,
        MatchingInvalidStatusException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ApiResponse.fail(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }
} 