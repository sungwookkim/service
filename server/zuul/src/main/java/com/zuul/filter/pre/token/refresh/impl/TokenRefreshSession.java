package com.zuul.filter.pre.token.refresh.impl;

import java.util.Map;
import java.util.Optional;

import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.context.RequestContext;
import com.zuul.filter.pre.token.produce.impl.TokenProduceSession;

public class TokenRefreshSession extends TokenRefreshDefault {

	public TokenRefreshSession() {}
	
	public TokenRefreshSession(String url, RestTemplate restTemplate, RequestContext requestContext,
			DefaultTokenServices defaultTokenServices, JwtTokenStore jwtTokenStore,
			JwtAccessTokenConverter jwtAccessTokenConverter) {
		
		super(url, restTemplate, requestContext, defaultTokenServices, jwtTokenStore, jwtAccessTokenConverter);
	}
	
	@Override
	protected void tokenSave(Map<String, Object> resultToken) {

		RequestContext requestContext = this.getRequestContext();
		
		new TokenProduceSession().tokenSave(resultToken, requestContext.getRequest(), requestContext.getResponse());
	}
	
	@Override
	protected String getToken(String searchTokenKey) {

		return Optional.ofNullable(this.getRequestContext().getRequest().getSession()
				.getAttribute(searchTokenKey))
			.map(st -> st.toString())
			.orElse("");						
	}
}