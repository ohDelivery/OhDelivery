package com.ohdelivery.server.authserver.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Tokens {
    private String accessToken;
    private String refreshToken;

    public static Tokens create(String accessToken, String refreshToken) {
        return new Tokens(accessToken, refreshToken);
    }
}
