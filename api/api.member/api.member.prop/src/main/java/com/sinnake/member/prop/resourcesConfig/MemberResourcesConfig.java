package com.sinnake.member.prop.resourcesConfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prop.resourcesConfig.ResourcesConfig;

public class MemberResourcesConfig implements ResourcesConfig {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers(HttpMethod.PUT, "/api/member/sign").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/api/member/logout").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/api/member/info").hasRole("USER")
			.anyRequest()
			.permitAll();
	}

}
