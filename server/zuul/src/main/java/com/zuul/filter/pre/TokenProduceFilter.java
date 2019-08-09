package com.zuul.filter.pre;

import java.util.function.Supplier;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.sinnake.entity.ResultEntity;
import com.zuul.config.prop.TokenControlConfig;
import com.zuul.filter.biz.init.UserInit;
import com.zuul.filter.pre.token.produce.impl.TokenProduceCookieSession;
import com.zuul.filter.pre.token.produce.impl.TokenProduceDefault;
import com.zuul.filter.pre.token.produce.impl.TokenProduceSession;

import config.serverList.ServerListProp;
import config.serverList.ServerListProp.ServerList;

public class TokenProduceFilter extends ZuulFilter {

	private RestTemplate restTemplate;
	private ServerListProp serverListProp;
	private ServerList oauthServerInfo;
	private TokenControlConfig tokenControlConfig;

	private static final String GRANT_TYPE = "password";
	private static final String LOGIN_URL = "/api/member/login";
	
	@Autowired
	public TokenProduceFilter(RestTemplate restTemplate
		, ServerListProp serverListProp
		, TokenControlConfig tokenControlConfig) {

		this.restTemplate = restTemplate;
		this.serverListProp = serverListProp;
		this.tokenControlConfig = tokenControlConfig;
		
		this.oauthServerInfo = this.serverListProp.getServerList("oauthServer");
	}

	@Override
	public boolean shouldFilter() {		
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest httpServletRequest = requestContext.getRequest();
		
		return "POST".equals(httpServletRequest.getMethod()) 
			&& LOGIN_URL.equals(httpServletRequest.getRequestURI());
	}

	@Override
	public Object run() throws ZuulException {
		ResultEntity<String> result;
		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest httpServletRequest = requestContext.getRequest();
		
		String url = this.oauthServerInfo.getHttpFullAddress("tokenOauth");
		String userName = httpServletRequest.getParameter("username");

		Supplier<LinkedMultiValueMap<String ,String>> tokenPostParam = () -> {
			return new LinkedMultiValueMap<String ,String>() {
				private static final long serialVersionUID = 1L;

				{
					add("username", userName);
					add("password", httpServletRequest.getParameter("password"));
					add("grant_type", GRANT_TYPE);
				}			
			};
		};
		
		new UserInit(requestContext.getRequest()
			, requestContext.getResponse()).init();

		switch (this.tokenControlConfig.getTokenControl()) {
			case "cookie":
				result = new TokenProduceDefault(url, userName, this.restTemplate, requestContext).produce(tokenPostParam);
				break;
			case "session" :
				result = new TokenProduceSession(url, userName, this.restTemplate, requestContext).produce(tokenPostParam);
				break;
			case "cookieSession" : 
				result = new TokenProduceCookieSession(url, userName, this.restTemplate, requestContext).produce(tokenPostParam);
				break;
			default:
				result = new TokenProduceDefault(url, userName, this.restTemplate, requestContext).produce(tokenPostParam);
				break;
		}
		
		if(!"200".equals(result.getCode())) {
			requestContext.setSendZuulResponse(false);
			requestContext.setRouteHost(null);
			requestContext.setResponseStatusCode(Integer.parseInt(result.getCode()));
			requestContext.setResponseBody(new ResultEntity<>(ResultEntity.failCodeString(), result.getResult()).toString());
		}

		return null;
	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}


}
