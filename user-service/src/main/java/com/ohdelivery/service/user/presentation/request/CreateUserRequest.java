package com.ohdelivery.service.user.presentation.request;

import com.ohdelivery.common.passport.RoleType;
import com.ohdelivery.service.user.application.command.CreateUserCommand;
import lombok.Getter;

@Getter
public class CreateUserRequest {
    String username;
    String name;
    String password;
    RoleType role;
    String slackId;

    public CreateUserCommand toCommand() {
        return CreateUserCommand.builder()
                .username(username)
                .name(name)
                .password(password)
                .role(role)
                .slackId(slackId)
                .build();
    }
}
