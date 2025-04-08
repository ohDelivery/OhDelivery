package com.ohdelivery.common.response;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class ApiResponse<T> {

    String code;
    String message;
    T data;


    public static <T> ApiResponse<T> success(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> success(String code, String message, T data) {
        return new ApiResponse<>(code, message, data);
    }

    public static <T> ApiResponse<T> fail(String code, String message) {
        return new ApiResponse<>(code, message, null);
    }

    public static <T> ApiResponse<T> fail(HttpStatus code, String message) {
        return new ApiResponse<>(code.toString(), message, null);
    }
}
