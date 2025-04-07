package com.ohdelivery.common.response;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SuccessCode {

	COMMON_SUCCESS(HttpStatus.OK, "요청에 대한 응답이 완료되었습니다."),
	CREATED_SUCCESS(HttpStatus.CREATED, "정상적으로 생성이 되었습니다.");

	private final HttpStatus code;
	private final String message;

}
