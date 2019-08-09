package com.zuul.filter.pre;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zuul.config.prop.TokenControlConfig;
import com.zuul.filter.pre.token.refresh.impl.TokenRefreshCookieSession;
import com.zuul.filter.pre.token.refresh.impl.TokenRefreshDefault;
import com.zuul.filter.pre.token.refresh.impl.TokenRefreshSession;

import config.serverList.ServerListProp;
import config.serverList.ServerListProp.ServerList;

public class TokenRefreshFilter extends ZuulFilter {

	private RestTemplate restTemplate;
	private DefaultTokenServices defaultTokenServices;
	private JwtTokenStore jwtTokenStore;
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	
	private ServerListProp serverListProp;
	private ServerList oauthServerInfo;
	private TokenControlConfig tokenControlConfig;
	
	private static final String GRANT_TYPE = "refresh_token";
	
	@Autowired
	public TokenRefreshFilter(RestTemplate restTemplate
		, ServerListProp serverListProp
		, DefaultTokenServices defaultTokenServices
		, JwtTokenStore jwtTokenStore
		, JwtAccessTokenConverter jwtAccessTokenConverter
		, TokenControlConfig tokenControlConfig) {

		this.restTemplate = restTemplate;
		this.serverListProp = serverListProp;
		this.defaultTokenServices = defaultTokenServices;
		this.jwtTokenStore = jwtTokenStore;
		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
		this.tokenControlConfig = tokenControlConfig;
		
		this.oauthServerInfo = this.serverListProp.getServerList("oauthServer");
	}
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext requestContext = RequestContext.getCurrentContext();
		String url = this.oauthServerInfo.getHttpFullAddress("tokenOauth");
		
		Supplier<LinkedMultiValueMap<String ,String>> tokenPostParam = () -> {
			return new LinkedMultiValueMap<String ,String>() {				
				private static final long serialVersionUID = 1L;
				
				{
					add("grant_type", GRANT_TYPE);
				}
			};	
		};

		switch (this.tokenControlConfig.getTokenControl()) {
			case "cookie":
				new TokenRefreshDefault(url, this.restTemplate, requestContext, this.defaultTokenServices
					, this.jwtTokenStore, this.jwtAccessTokenConverter)
				.refresh(tokenPostParam);
				break;
			case "session" :
				new TokenRefreshSession(url, this.restTemplate, requestContext, this.defaultTokenServices
					, this.jwtTokenStore, this.jwtAccessTokenConverter)
				.refresh(tokenPostParam);
				break;
			case "cookieSession" : 
				new TokenRefreshCookieSession(url, this.restTemplate, requestContext, this.defaultTokenServices
					, this.jwtTokenStore, this.jwtAccessTokenConverter)
				.refresh(tokenPostParam);
				break;
			default:
				new TokenRefreshDefault(url, this.restTemplate, requestContext, this.defaultTokenServices
					, this.jwtTokenStore, this.jwtAccessTokenConverter)
				.refresh(tokenPostParam);
				break;
		}

		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
