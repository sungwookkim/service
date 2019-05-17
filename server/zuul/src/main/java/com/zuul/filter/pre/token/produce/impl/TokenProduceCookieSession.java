package com.zuul.filter.pre.token.produce.impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.context.RequestContext;
import com.util.CookieUtil;
import com.zuul.filter.pre.token.enumPackage.TokenKey;

public class TokenProduceCookieSession extends TokenProduceDefault {

	public TokenProduceCookieSession() {}
	
	public TokenProduceCookieSession(String url, String userName, RestTemplate restTemplate, RequestContext requestContext) {
		
		super(url, userName, restTemplate, requestContext);
	}
	
	@Override
	public void tokenSave(Map<String, Object> resultToken) {

		RequestContext requestContext = this.getRequestContext();
		
		this.tokenSave(resultToken, requestContext.getRequest(), requestContext.getResponse());
	}
	
	public void tokenSave(Map<String, Object> resultToken, HttpServletRequest req, HttpServletResponse resp) {

		String accessToken = resultToken.get(TokenKey.RESULT_ACCESS_TOKEN.getKey()).toString();
		String refreshToken = resultToken.get(TokenKey.RESULT_REFRESH_TOKEN.getKey()).toString();

		CookieUtil.setCookie(TokenKey.TOKEN_SAVE_ACCESS_TOKEN.getKey(), accessToken, c -> {
			c.setPath("/");
			c.setHttpOnly(true);
			// c.setSecure(true);
			resp.addCookie(c);
		});

		CookieUtil.setCookie(TokenKey.TOKEN_SAVE_REFRESH_TOKEN.getKey(), null, c -> {
			c.setPath("/");
			c.setMaxAge(0);
			resp.addCookie(c);
		});
		
		req.getSession().setAttribute(TokenKey.TOKEN_SAVE_REFRESH_TOKEN.getKey(), refreshToken);
	}	
}