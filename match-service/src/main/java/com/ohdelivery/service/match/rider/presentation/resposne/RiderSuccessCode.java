package com.ohdelivery.service.match.rider.presentation.resposne;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RiderSuccessCode {
  RIDER_STATUS_CHANGE_SUCCESS(HttpStatus.OK, "라이더 상태 변경 성공");

  private final HttpStatus code;
  private final String message;
}

