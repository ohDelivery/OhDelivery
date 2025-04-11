package com.ohdelivery.service.match.alarm.application.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AlarmErrorCode {
  SLACK_USER_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "슬랙 사용자 ID를 찾을 수 없습니다."),
  SLACK_CHANNEL_OPEN_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 채널을 열 수 없습니다."),
  SLACK_MESSAGE_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "슬랙 메시지 전송에 실패했습니다."),
  ;

  private final HttpStatus httpStatus;
  private final String message;
}
