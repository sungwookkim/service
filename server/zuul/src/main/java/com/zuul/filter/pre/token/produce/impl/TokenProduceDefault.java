package com.zuul.filter.pre.token.produce.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.zuul.context.RequestContext;
import com.sinnake.entity.ResultEntity;
import com.util.CookieUtil;
import com.zuul.filter.pre.token.enumPackage.TokenKey;
import com.zuul.filter.pre.token.produce.abs.TokenProduce;

public class TokenProduceDefault extends TokenProduce<String> {

	public TokenProduceDefault() {}
	
	public TokenProduceDefault(String url, String userName, RestTemplate restTemplate, RequestContext requestContext) {

		super(url, userName, restTemplate, requestContext);
	}

	@Override
	protected ResultEntity<String> produceCall(ResponseEntity<String> response) {

		Map<String, Object> body = new HashMap<>();
		body.put("code", 0);

		return new ResultEntity<>(String.valueOf(response.getStatusCodeValue())
			, new Gson().toJson(body, Map.class));
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ResultEntity<String> produceFail(Exception e) {

		e.printStackTrace();

		if (e instanceof HttpStatusCodeException) {
			HttpStatusCodeException httpStatusCodeException = (HttpStatusCodeException) e;
			HttpStatus httpStatus = httpStatusCodeException.getStatusCode();
			Map<String, Object> body = new HashMap<>();
			
			if(httpStatus == HttpStatus.UNAUTHORIZED) {
				body.put("code", "-7");
				body.put("erros", Arrays.asList("unauthorized_client", httpStatus.toString()));
			} else {
				body = new Gson().fromJson(httpStatusCodeException.getResponseBodyAsString(), HashMap.class);
			}

			return new ResultEntity<>(String.valueOf(httpStatus.value()),  new Gson().toJson(body));					
		}
		
		return new ResultEntity<>("500");
	}

	@Override
	public void tokenSave(Map<String, Object> resultToken) {

		RequestContext requestContext = this.getRequestContext();
		
		this.tokenSave(resultToken, requestContext.getRequest(), requestContext.getResponse());
	}
	
	public void tokenSave(Map<String, Object> resultToken, HttpServletRequest req, HttpServletResponse resp) {

		String accessToken = resultToken.get(TokenKey.RESULT_ACCESS_TOKEN.getKey()).toString();
		String refreshToken = resultToken.get(TokenKey.RESULT_REFRESH_TOKEN.getKey()).toString();

		req.getSession().removeAttribute(TokenKey.TOKEN_SAVE_ACCESS_TOKEN.getKey());
		req.getSession().removeAttribute(TokenKey.TOKEN_SAVE_REFRESH_TOKEN.getKey());
		
		CookieUtil.setCookie(TokenKey.TOKEN_SAVE_ACCESS_TOKEN.getKey(), accessToken, c -> {
			c.setPath("/");
			c.setHttpOnly(true);
			// c.setSecure(true);
			resp.addCookie(c);
		});
		
		CookieUtil.setCookie(TokenKey.TOKEN_SAVE_REFRESH_TOKEN.getKey(), refreshToken, c -> {
			c.setPath("/");
			c.setHttpOnly(true);
			// c.setSecure(true);
			resp.addCookie(c);
		});
	}

}
