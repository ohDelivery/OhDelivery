package com.ohdelivery.common.passport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Passport {
	private String userId;
	private RoleType roleType;
}
