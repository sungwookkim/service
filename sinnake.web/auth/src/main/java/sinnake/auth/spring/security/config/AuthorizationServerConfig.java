package sinnake.auth.spring.security.config;

import java.security.KeyPair;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import sinnake.auth.spring.security.oauth2Exception.SinnakeOauth2Exception;
import sinnake.auth.spring.security.userDetailDao.SinnakeUserDetailService;

/**
 * oauth 설정 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	private AuthenticationManager authenticationManager;	
	private SinnakeUserDetailService sinnakeUserDetailService;
	// private OAuth2RequestFactory oAuth2RequestFactory = null;
	private ClientDetailsService clientDetailsService = null;
	
	/**
	 * 
	 * @param sinnakeUserDetailService 사용자 정보 조회 등 관련 커스텀 클래스.
	 * @param authenticationManager 인증 관련 매니저 클래스.
	 * @param clientDetailsService 인증 정보 서비스 클래스.
	 */
	@Autowired
	public AuthorizationServerConfig(SinnakeUserDetailService sinnakeUserDetailService
		, AuthenticationManager authenticationManager
		, ClientDetailsService clientDetailsService) {
	
		this.sinnakeUserDetailService = sinnakeUserDetailService;
		this.authenticationManager = authenticationManager;
		this.clientDetailsService = clientDetailsService;
	}
	
	/**
	 * JWT Store 생성 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link TokenStore}
	 */
    @Bean
    public TokenStore tokenStore() {    	
        return new JwtTokenStore(accessTokenConverter());
    }
	
    /**
     * JWT 생성 메서드.
     * 
     * @author sinnakeWEB
     * @return {@link JwtAccessTokenConverter}
     */
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("cert/server.jks"), "passtwo".toCharArray())
				.getKeyPair("auth", "passone".toCharArray());
		
		converter.setKeyPair(keyPair);
		
		return converter;
	}
	
	/**
	 * JWT 서비스 설정 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link DefaultTokenServices}
	 */
	@Bean
	@Primary
	public DefaultTokenServices tokenService() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		defaultTokenServices.setReuseRefreshToken(false);
		defaultTokenServices.setSupportRefreshToken(true);

		return defaultTokenServices;
	}

	/**
	 * oauth endpoint 설정 메서드.
	 * 
	 * @param endpoints oauth endpoint의 전반적인 설정을 할 수 있다.
	 * @throws Exception 예외 발생.
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// this.oAuth2RequestFactory = endpoints.getOAuth2RequestFactory();

		endpoints
			.tokenStore(tokenStore())
			.authenticationManager(authenticationManager)
			.userDetailsService(sinnakeUserDetailService)
			.accessTokenConverter(accessTokenConverter())
			.exceptionTranslator(exception -> {
	            if (exception instanceof OAuth2Exception) {
	                OAuth2Exception oAuth2Exception = (OAuth2Exception) exception;
	                return ResponseEntity
	                        .status(oAuth2Exception.getHttpErrorCode())
	                        .body(new SinnakeOauth2Exception(oAuth2Exception.getMessage()));
	            } else {
	                throw exception;
	            }				
			});
	}

	/**
	 * 사용자 설정 메서드.
	 * 
	 * @author sinnakeWEB
	 * @param clients 사용자와 관련된 전반적인 설정을 할 수 있다.
	 * @throws Exception 예외 발생.
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.withClientDetails(clientDetailsService);
	}

	/**
	 * oauth 설정 메서드.
	 * @author sinnakeWEB 
	 * @param security oauth의 전반적인 설정을 할 수 있다.
	 */
	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
			.tokenKeyAccess("permitAll()")
			.checkTokenAccess("permitAll()");

		//security.addTokenEndpointAuthenticationFilter(new SinnakeTokenEndpointAuthenticationFilter(authenticationManager, this.oAuth2RequestFactory));
	}
	
	/**
	 * 인증에 대한 정보를 DB로 전환하기 위한 메서드.
	 * 
	 * @param dataSource DataSource
	 * @return {@link JdbcClientDetailsService}
	 */
	@Bean
	@Primary
	public JdbcClientDetailsService JdbcClientDetailsService(DataSource authDataSource) {
		return new JdbcClientDetailsService(authDataSource);
	}
}
