package com.ohdelivery.server.authserver.presentation.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}
