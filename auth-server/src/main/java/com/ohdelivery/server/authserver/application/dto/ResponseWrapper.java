package com.ohdelivery.server.authserver.application.dto;

import lombok.Getter;

@Getter
public class ResponseWrapper {
    String code;
    String message;
    UserInfo data;
}
