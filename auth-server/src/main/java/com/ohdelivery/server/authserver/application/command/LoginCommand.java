package com.ohdelivery.server.authserver.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginCommand {
    private String username;
    private String password;

    public static LoginCommand create(String username, String password) {
        return new LoginCommand(username, password);
    }
}
