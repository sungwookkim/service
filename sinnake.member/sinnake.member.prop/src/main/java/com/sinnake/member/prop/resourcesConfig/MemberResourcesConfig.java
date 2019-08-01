package com.sinnake.member.prop.resourcesConfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prop.resourcesConfig.ResourcesConfig;

public class MemberResourcesConfig implements ResourcesConfig {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
			.antMatchers(HttpMethod.PUT, "/api/member/sign").hasRole("USER")
			.antMatchers("/api/member/authtemporary").hasRole("USER")
			.anyRequest()
			.permitAll();
	}

}
