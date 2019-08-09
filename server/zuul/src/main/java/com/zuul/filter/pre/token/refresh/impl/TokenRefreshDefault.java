package com.zuul.filter.pre.token.refresh.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.Cookie;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import com.sinnake.entity.ResultEntity;
import com.zuul.filter.enums.TokenKey;
import com.zuul.filter.pre.token.produce.impl.TokenProduceDefault;
import com.zuul.filter.pre.token.refresh.abs.TokenRefresh;

import util.RestProcess;

public class TokenRefreshDefault extends TokenRefresh<String> {
	private DefaultTokenServices defaultTokenServices;
	private JwtTokenStore jwtTokenStore;
	private JwtAccessTokenConverter jwtAccessTokenConverter;
	private Cookie[] cookies;
	
	public TokenRefreshDefault() {}
	
	public TokenRefreshDefault(String url, RestTemplate restTemplate, RequestContext requestContext
			, DefaultTokenServices defaultTokenServices , JwtTokenStore jwtTokenStore , JwtAccessTokenConverter jwtAccessTokenConverter) {
		
		super(url, restTemplate, requestContext);
		
		this.defaultTokenServices = defaultTokenServices;
		this.jwtTokenStore = jwtTokenStore;
		this.jwtAccessTokenConverter = jwtAccessTokenConverter;
		
		cookies = this.getRequestContext().getRequest().getCookies();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ResultEntity<ResponseEntity<String>> refreshCall(ResponseEntity<String> response) {

		Map<String, Object> resultToken = new Gson().fromJson(response.getBody(), HashMap.class);			
		String accessToken = resultToken.get(TokenKey.RESULT_ACCESS_TOKEN.getKey()).toString();

		this.getRequestContext().addZuulRequestHeader("authorization", "bearer " + accessToken);
		
		return new ResultEntity<>(ResultEntity.sucessCodeString(), response);
	}

	@Override
	protected ResultEntity<ResponseEntity<String>> refreshFail(Exception e) {

		e.printStackTrace();

		HashMap<String, Object> err = new HashMap<>();
		err.put("code", -899);
		err.put("erros", "");
		
		this.getRequestContext().setSendZuulResponse(false);
		this.getRequestContext().setRouteHost(null);
		this.getRequestContext().setResponseStatusCode(500);
		this.getRequestContext().setResponseBody(new Gson().toJson(err));
		
		if (e instanceof HttpStatusCodeException) {
			HttpStatusCodeException httpStatusCodeException = (HttpStatusCodeException) e;
			HttpStatus httpStatus = httpStatusCodeException.getStatusCode();
			String errBody = httpStatusCodeException.getResponseBodyAsString();
			
			this.getRequestContext().setResponseStatusCode(httpStatusCodeException.getStatusCode().value());

			if(httpStatus == HttpStatus.UNAUTHORIZED) {
				err.put("code", -898);
				err.put("erros", Arrays.asList("unauthorized_client", httpStatus.toString()));
				
				errBody = new Gson().toJson(err);
			} 
			
			this.getRequestContext().setResponseBody(errBody);
		}
		
		return new ResultEntity<>(ResultEntity.failCodeString());
	}

	@Override
	protected String getAccessToken() {
		
		return this.getToken(TokenKey.TOKEN_SAVE_ACCESS_TOKEN.getKey());
	}

	@Override
	protected String getRefreshToken() {
		
		return this.getToken(TokenKey.TOKEN_SAVE_REFRESH_TOKEN.getKey());
	}
	
	@Override
	protected Map<String, ?> checkToken(String accessToken) {

		int commonStatusCode = -999;
		
		return new RestProcess<HashMap<String, Object>>()
			.throwUsed()
			.call(() -> {
				HashMap<String, Object> rtn = new HashMap<>();
				rtn.put("statusCode", commonStatusCode);
				rtn.put("data", "");
				
				this.jwtTokenStore.setTokenEnhancer(this.jwtAccessTokenConverter);
				this.defaultTokenServices.setTokenStore(this.jwtTokenStore);

				OAuth2AccessToken token = this.defaultTokenServices.readAccessToken(accessToken);
				
				int statusCode = Optional.ofNullable(token)
					.map(t -> t.isExpired() ? -998 : 0)
					.orElse(-997);

				rtn.put("statusCode", statusCode);

				if(statusCode == 0) {
					OAuth2Authentication authentication =  this.defaultTokenServices.loadAuthentication(token.getValue());

					rtn.put("data", new DefaultAccessTokenConverter().convertAccessToken(token, authentication));				
				}
				
				return new ResultEntity<HashMap<String,Object>>(ResultEntity.sucessCodeString(), rtn);
			})
			.fail(e -> {
				HashMap<String, Object> rtn = new HashMap<>();
				rtn.put("statusCode", commonStatusCode);
				rtn.put("data", "");
				
				if(e instanceof OAuth2Exception) {
					rtn.put("statusCode", -996);
					rtn.put("data", "{" + ((OAuth2Exception)e).getSummary() + "}");
				} else {
					e.printStackTrace();	
				}

				return new ResultEntity<>(ResultEntity.failCodeString(), rtn);
			})
			.exec()
			.getResult();
	}

	@Override
	protected void tokenSave(Map<String, Object> resultToken) {

		RequestContext requestContext = this.getRequestContext();
		
		new TokenProduceDefault().tokenSave(resultToken, requestContext.getRequest(), requestContext.getResponse());
	}
	
	protected String getToken(String searchTokenKey) {

		return Optional.ofNullable(this.cookies)
			.map(cs -> {
				String cookieVal = "";
				
				for(Cookie cookie : cs) {
					if(searchTokenKey.equals(cookie.getName())) {
						cookieVal = cookie.getValue();
						break;
					}
				}
				
				return cookieVal;
			})
			.orElse("");
	}	
}
