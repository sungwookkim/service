package com.sinnake.categories.prop.resourcesConfig;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import prop.resourcesConfig.ResourcesConfig;

public class CategoriesResourcesConfig implements ResourcesConfig {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/api/v1/categories/**").hasRole("ADMIN")
			.antMatchers(HttpMethod.POST, "/api/v1/searchOption/kind").hasRole("ADMIN")
			.antMatchers(HttpMethod.PUT, "/api/v1/searchOption/kind/**").hasRole("ADMIN")
			.anyRequest()
			.permitAll();
	}

}
