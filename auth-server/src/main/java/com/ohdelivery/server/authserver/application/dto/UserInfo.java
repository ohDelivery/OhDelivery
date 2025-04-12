package com.ohdelivery.server.authserver.application.dto;

import com.ohdelivery.common.passport.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserInfo {
    String id;
    RoleType role;

    public static UserInfo create(String subject, String role) {
        return new UserInfo(subject, RoleType.fromAuthority(role));
    }
}
