package com.token.tokenServices;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

public class SinnakeDefaultTokenServices extends DefaultTokenServices {

	private TokenStore sinnakeTokeStore;
	
	public SinnakeDefaultTokenServices() { }
	
	@Override
	public OAuth2Authentication loadAuthentication(String accessTokenValue) throws AuthenticationException,
		InvalidTokenException {

		OAuth2Authentication result = null;
		try {
			result = super.loadAuthentication(accessTokenValue);
		} catch(InvalidTokenException e) {
			if(e.getMessage().indexOf("Access token expired") == -1 ) {
				throw e;
			}

			result = sinnakeTokeStore.readAuthentication(
				sinnakeTokeStore.readAccessToken(accessTokenValue));
		}
		
		return result;
	}
	
	@Override
	public void setTokenStore(TokenStore tokenStore) {
		this.sinnakeTokeStore = tokenStore;
		
		super.setTokenStore(tokenStore);		
	}
}
