package com.sinnake.product.prop.resourcesConfig;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prop.resourcesConfig.ResourcesConfig;

public class ProductResourcesConfig implements ResourcesConfig {

	@Override
	public void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()			
			/*.antMatchers("/api/member/authproduct").hasRole("USER")*/
			.anyRequest()
			.permitAll();
	}

}
