package com.zuul;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import com.netflix.loadbalancer.PollingServerListUpdater;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ServerList;
import com.netflix.loadbalancer.ServerListFilter;
import com.netflix.loadbalancer.ZoneAwareLoadBalancer;
import com.zuul.config.Config;
import com.zuul.filter.Filter;

@EnableZuulProxy
@EnableEurekaClient
@EnableRedisHttpSession
@EnableResourceServer
@SpringBootApplication
@ComponentScan(basePackages = {"config"})
@RibbonClients(defaultConfiguration = RibbonConfig.class)

@Import({
	Config.class
	, Filter.class
	, RedisConfig.class
	, SecurityConfig.class
})
public class ZuulServerApplication {
		
	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}
	
	@Bean
	public TomcatServletWebServerFactory containerFactory() {
		TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
		
		tomcatServletWebServerFactory.addConnectorCustomizers(connector -> {
			((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setConnectionTimeout(10000);
		});
		
		return tomcatServletWebServerFactory; 
	}
	
}

class RibbonConfig {
	@Bean 
	public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter,
			IRule rule, IPing ping) {

		return new ZoneAwareLoadBalancer<>(config, rule, ping
				, serverList, serverListFilter, new PollingServerListUpdater(config));
	}
	
	@Bean
	public IPing ribbonPing(IClientConfig config) {
		return new PingUrl(false, "/actuator/health");
	}
}

class RedisConfig {

	@Value("${spring.session.timeout.seconds}")
	private Integer springSessionTimeOut;
	
	@Bean
	public static ConfigureRedisAction configureRedisAction() {
		return ConfigureRedisAction.NO_OP;
	}
	
	@Bean
	public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory
		, RedisOperationsSessionRepository messageListener) {
		
		messageListener.setDefaultMaxInactiveInterval(springSessionTimeOut);
		return new RedisMessageListenerContainer();
	} 
}

class SecurityConfig extends ResourceServerConfigurerAdapter {
	
	private String publicKey;
	
	@Autowired
	public SecurityConfig(@Value("#{globalProp['publicKey']}") String publicKey) {
		
		this.publicKey = publicKey;
	}
	
	@Bean
	public TokenStore tokenStore() {
		return new JwtTokenStore(accessTokenConverter());
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		
		converter.setVerifierKey(publicKey);
		
		return converter;
	}
	
	@Bean
	@Primary
	public DefaultTokenServices tokenService() {
		DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
		defaultTokenServices.setTokenStore(tokenStore());
		
		return defaultTokenServices;
	}
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
			.csrf()
				.disable()			
			.authorizeRequests()
			.anyRequest()
			.permitAll();
	}	
}
