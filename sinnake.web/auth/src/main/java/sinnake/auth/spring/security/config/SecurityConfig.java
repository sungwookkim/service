package sinnake.auth.spring.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import sinnake.auth.spring.security.authenticationProvider.MemberPwdAuthenticationProvider;

/**
 * Spring security 설정 클래스.
 * 
 * @author sinnakeWEB
 */
@SuppressWarnings("deprecation")
@Configuration
@EnableWebMvcSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private AuthenticationProvider memberPwdAuthenticationProvider;
    
	@Autowired
	public SecurityConfig(MemberPwdAuthenticationProvider memberPwdAuthenticationProvider) {

		// 회원 비밀번호 검증.
		this.memberPwdAuthenticationProvider = memberPwdAuthenticationProvider;
	}
	
	/**
	 * Spring Security 인증 처리에 대한 설정을 할 수 있는 메서드.
	 * 
	 * @author sinnakeWEB
	 * @param auth 인증 처리에 대한 전반적인 설정을 할 수 있다.
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(memberPwdAuthenticationProvider);
	}
    
	/**
	 * AuthenticationManager을 반환한다.
	 * 
	 * @author sinnakeWEB
	 * @return {@link AuthenticationManager}
	 * @throws Exception 예외 발생.
	 */
	@Bean
	@Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }	
	
	/**
	 * 접근 및 보안 설정을 할 수 있는 메서드.
	 * 
	 * @param http 접근에 대한 인증 및 권한 그리고 보안에 대한 전반적인 설정을 할 수 있다.
	 * @throws Exception 예외 발생.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
				.authorizeRequests()
				.antMatchers("/oauth/oauth")					
				.permitAll()
				.anyRequest()
				.anonymous();

	}

/*    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }*/
}
