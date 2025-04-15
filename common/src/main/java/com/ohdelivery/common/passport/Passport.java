package com.ohdelivery.common.passport;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Passport {
	private String userId;
	private RoleType roleType;
}
