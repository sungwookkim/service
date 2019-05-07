package com.sinnake.member.web.spring;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.member", "com.sinnake", "config"})
public class SinnakeMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(SinnakeMemberApplication.class, args);
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