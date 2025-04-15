package com.ohdelivery.server.authserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenStatus {
    INVALID("로그인 정보가 유효하지 않습니다."),
    LOGGED_OUT("로그아웃 되었습니다."),
    EXPIRED("만료된 로그인입니다.")
    ;

    final String message;

}
