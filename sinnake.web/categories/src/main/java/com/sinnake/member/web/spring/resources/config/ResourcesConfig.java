package com.sinnake.member.web.spring.resources.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * resources 설정 클래스.
 * 
 * @author sinnakeWEB
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableResourceServer
@EnableWebMvcSecurity
public class ResourcesConfig extends ResourceServerConfigurerAdapter {
	private String publicKey;
	
	@Autowired
	public ResourcesConfig(@Value("#{webGlobalProp['publicKey']}") String publicKey) {
		
		this.publicKey = publicKey;
	}

	/**
	 * 토큰 스토어 설정 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return
	 */
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	/**
	 * 토큰 변환 설정 메서드.
	 * 
	 * @author sinnakeWEB 
	 * @return
	 */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		
		converter.setVerifierKey(publicKey);
		
		return converter;
	}
	
	/**
	 * 토큰 서비스 설정 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return
	 */	
	@Bean
	@Primary
	public DefaultTokenServices tokenService() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setSupportRefreshToken(true);
		
		return defaultTokenServices;
	}	
	
	/**
	 * 접근 및 보안 설정을 할 수 있는 메서드.
	 * 
	 * @param http 접근에 대한 인증 및 권한 그리고 보안에 대한 전반적인 설정을 할 수 있다.
	 * @throws Exception 예외 발생. 
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers(HttpMethod.POST, "/api/categories/add/*").hasRole("ADMIN")				
				.anyRequest()
				.permitAll();
	}
	
	/**
	 * Spring Resource 처리에 대한 설정을 할 수 있는 메서드.
	 * 
	 * @author sinnakeWEB 
	 */
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		
	}

}
