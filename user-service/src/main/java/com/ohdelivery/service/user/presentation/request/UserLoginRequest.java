package com.ohdelivery.service.user.presentation.request;

import com.ohdelivery.service.user.application.command.UserLoginCommand;
import lombok.Getter;

@Getter
public class UserLoginRequest {
    String username;
    String password;

    public UserLoginCommand toCommand() {
        return UserLoginCommand.create(username, password);
    }
}
