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
}
