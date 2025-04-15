package com.ohdelivery.common.passport;

public enum RoleType {
	RIDER("Role_Rider"),
	AGENT("Role_Agent"),
	MASTER("Role_Master"),
	STORE("Role_Store")
	;

	private final String authority;

	RoleType(String authority) {
		this.authority = authority;
	}

	public String getAuthority() {
		return this.authority;
	}

	public static RoleType fromAuthority(String authority) {
		for (RoleType role : RoleType.values()) {
			if (role.getAuthority().equals(authority)) {
				return role;
			}
		}
		throw new IllegalArgumentException(authority + "역할에 대한 권한이 없습니다");
	}
}
