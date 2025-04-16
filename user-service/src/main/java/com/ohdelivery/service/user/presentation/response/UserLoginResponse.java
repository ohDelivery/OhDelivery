package com.ohdelivery.service.user.presentation.response;

import com.ohdelivery.common.passport.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    Long id;
    RoleType role;

    public static UserLoginResponse create(Long id, RoleType role) {
        return new UserLoginResponse(id, role);
    }
}
