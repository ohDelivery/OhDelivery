package com.ohdelivery.service.user.presentation.response;

import com.ohdelivery.common.passport.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthResponse {
    Long id;
    RoleType role;
}
