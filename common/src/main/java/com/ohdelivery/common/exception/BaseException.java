package com.ohdelivery.common.exception;

import static com.ohdelivery.common.exception.ErrorCode.*;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final ErrorCode errorCode;

	// 에러 메시지를 받는 생성자
	public BaseException(String message) {
		super(message);
		this.errorCode = INTERNAL_SERVER_EXCEPTION;
	}

	// 에러 코드를 지정하는 생성자
	public BaseException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	// 에러 코드와 메시지를 받는 생성자
	public BaseException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

}