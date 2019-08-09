package com.zuul.filter.biz.init;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserInit {
	private HttpServletRequest req;
	private HttpServletResponse resp;
	
	public UserInit(HttpServletRequest req, HttpServletResponse resp) {

		this.req = req;
		this.resp = resp;
	}
	
	public void init() {
		
		this.sessionRemove();
		this.cookieRemove();		
	}
	
	protected void cookieRemove() {

		Optional.ofNullable(req.getCookies())
			.filter(cs -> cs.length > 0)
			.map(c -> Arrays.asList(c))
			.orElseGet(ArrayList::new)
		.stream()
			.forEach(c -> {
				c.setPath("/");
				c.setMaxAge(0);

				this.resp.addCookie(c);
			});				
	}
	
	protected void sessionRemove() {
		
		Optional<HttpSession> session = Optional.ofNullable(req.getSession());
		if(session.isPresent()) {
			session.get().invalidate();
		}
					
	}
}
