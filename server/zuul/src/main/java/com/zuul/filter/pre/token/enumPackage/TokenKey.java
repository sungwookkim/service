package com.zuul.filter.pre.token.enumPackage;

public enum TokenKey {

	RESULT_ACCESS_TOKEN("access_token")
	, RESULT_REFRESH_TOKEN("refresh_token")
	
	, TOKEN_SAVE_ACCESS_TOKEN("accessToken")
	, TOKEN_SAVE_REFRESH_TOKEN("refreshToken");

	private String key;
	
	private TokenKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return this.key;
	}
}
