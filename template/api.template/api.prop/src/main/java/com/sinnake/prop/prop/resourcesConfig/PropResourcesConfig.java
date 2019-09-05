package com.sinnake.prop.prop.resourcesConfig;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prop.resourcesConfig.ResourcesConfig;

public class PropResourcesConfig implements ResourcesConfig {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()			
			/*.antMatchers("/api/v1/member/authtemporary").hasRole("USER")*/
			.anyRequest()
			.permitAll();
	}

}
