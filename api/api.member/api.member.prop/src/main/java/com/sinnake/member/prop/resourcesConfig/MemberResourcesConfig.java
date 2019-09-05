package com.sinnake.member.prop.resourcesConfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prop.resourcesConfig.ResourcesConfig;

public class MemberResourcesConfig implements ResourcesConfig {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers(HttpMethod.PUT, "/api/v1/member/sign").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/api/v1/member/logout").hasRole("USER")
			.antMatchers(HttpMethod.GET, "/api/v1/member/info").hasRole("USER")
			.anyRequest()
			.permitAll();
	}

}
