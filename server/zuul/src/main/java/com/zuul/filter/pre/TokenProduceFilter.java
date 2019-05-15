package com.zuul.filter.pre;

import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sinnake.entity.ResultEntity;
import com.util.CookieUtil;

import config.serverList.ServerListProp;
import config.serverList.ServerListProp.ServerList;
import util.RestProcess;

public class TokenProduceFilter extends ZuulFilter {

	private RestTemplate restTemplate;
	private ServerListProp serverListProp;
	private ServerList oauthServerInfo;
	private static final String GRANT_TYPE = "password";
	private static final String LOGIN_URL = "/member/login";
	
	@Autowired
	public TokenProduceFilter(RestTemplate restTemplate
		, ServerListProp serverListProp) {

		this.restTemplate = restTemplate;
		this.serverListProp = serverListProp;
		
		this.oauthServerInfo = this.serverListProp.getServerList("oauthServer");
	}

	@Override
	public boolean shouldFilter() {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest httpServletRequest = requestContext.getRequest();
		
		return "POST".equals(httpServletRequest.getMethod()) && LOGIN_URL.equals(httpServletRequest.getRequestURI());
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest httpServletRequest = requestContext.getRequest();

		requestContext.setSendZuulResponse(false);
		requestContext.setRouteHost(null);

		@SuppressWarnings({"unchecked" })
		ResultEntity<String> result = new RestProcess<String>()
			.call(() -> {
				String url = this.oauthServerInfo.getHttpFullAddress("tokenOauth");
				HttpHeaders httpHeaders = new HttpHeaders();
				String userName = httpServletRequest.getParameter("username");
				
				httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
				httpHeaders.add("authorization", "Basic " + Base64.getEncoder().encodeToString((userName + ":" + userName).getBytes()));							

				ResponseEntity<String> response = null;
				
				response = this.restTemplate.exchange(url
					, HttpMethod.POST
					, new HttpEntity<>(new LinkedMultiValueMap<String ,String>() {
						private static final long serialVersionUID = 1L;
						{
							add("username", userName);
							add("password", httpServletRequest.getParameter("password"));
							add("grant_type", GRANT_TYPE);
						}
					} , httpHeaders)
					, String.class);

				Map<String, Object> resultToken = new Gson().fromJson(response.getBody(), HashMap.class);
				String accessToken = resultToken.get("access_token").toString();
				String refreshToken = resultToken.get("refresh_token").toString();
				
				CookieUtil.setCookie("accessToken", accessToken, (c) -> {
					c.setPath("/");
					c.setHttpOnly(true);
					// c.setSecure(true);
					requestContext.getResponse().addCookie(c);
				});
				
				httpServletRequest.getSession().setAttribute("refreshToken", refreshToken);

				return new ResultEntity<>(String.valueOf(response.getStatusCodeValue()), "{code : 0}");
			})
			.fail(e -> {
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
			})
			.exec();
		
		requestContext.setResponseStatusCode(Integer.parseInt(result.getCode()));
		requestContext.setResponseBody(new ResultEntity<>("200".equals(result.getCode()) ? ResultEntity.ResultCode.SUCESS.getCode() 
			: ResultEntity.ResultCode.FAIL.getCode()
			, result.getResult()).toString());
		
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
