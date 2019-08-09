package com.zuul.filter.post;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.zuul.filter.biz.init.UserInit;

public class LogoutFilter extends ZuulFilter {

	private static final String LOGOUT_URL = "/api/member/logout";

	@Autowired
	public void Logoutfilter() {
	
	}

	@Override
	public boolean shouldFilter() {

		RequestContext requestContext = RequestContext.getCurrentContext();
		HttpServletRequest httpServletRequest = requestContext.getRequest();
		
		return "GET".equals(httpServletRequest.getMethod()) 
			&& LOGOUT_URL.equals(httpServletRequest.getRequestURI());
	}

	@Override
	public Object run() throws ZuulException {

		RequestContext requestContext = RequestContext.getCurrentContext();		
		
		new UserInit(requestContext.getRequest()
			, requestContext.getResponse()).init();
		
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

}
