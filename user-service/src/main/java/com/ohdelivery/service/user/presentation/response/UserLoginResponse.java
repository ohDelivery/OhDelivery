package com.ohdelivery.service.user.presentation.response;

import com.ohdelivery.common.passport.RoleType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserLoginResponse {
    Long id;
    RoleType role;
}
