package com.ohdelivery.service.user.application.command;

import com.ohdelivery.common.passport.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserCommand {
    String username;
    String name;
    String password;
    RoleType role;
    String slackEmail;

}
