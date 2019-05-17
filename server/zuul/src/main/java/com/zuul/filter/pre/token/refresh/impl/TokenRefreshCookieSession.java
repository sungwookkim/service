package com.zuul.filter.pre.token.refresh.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.context.RequestContext;
import com.zuul.filter.pre.token.enumPackage.TokenKey;
import com.zuul.filter.pre.token.produce.impl.TokenProduceCookieSession;

public class TokenRefreshCookieSession extends TokenRefreshDefault {

	public TokenRefreshCookieSession() {}
	
	public TokenRefreshCookieSession(String url, RestTemplate restTemplate, RequestContext requestContext,
			DefaultTokenServices defaultTokenServices, JwtTokenStore jwtTokenStore,
			JwtAccessTokenConverter jwtAccessTokenConverter) {
		
		super(url, restTemplate, requestContext, defaultTokenServices, jwtTokenStore, jwtAccessTokenConverter);
	}
	
	@Override
	protected void tokenSave(Map<String, Object> resultToken) {

		RequestContext requestContext = this.getRequestContext();
		
		new TokenProduceCookieSession().tokenSave(resultToken, requestContext.getRequest(), requestContext.getResponse());
	}
	
	@Override
	protected String getRefreshToken() {

		return Optional.ofNullable(this.getRequestContext().getRequest().getSession()
				.getAttribute(TokenKey.TOKEN_SAVE_REFRESH_TOKEN.getKey()))
			.map(t -> t.toString())
			.orElse("");
	}	
}