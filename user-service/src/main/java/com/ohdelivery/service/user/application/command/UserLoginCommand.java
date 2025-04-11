package com.ohdelivery.service.user.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginCommand {

    private String username;
    private String password;

    public static UserLoginCommand create(String username, String password) {
        return new UserLoginCommand(username, password);
    }
}
