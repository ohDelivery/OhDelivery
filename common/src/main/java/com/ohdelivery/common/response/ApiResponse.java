package com.ohdelivery.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
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
}
