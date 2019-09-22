package com.sinnake.member.web.spring;

import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import util.SinnakeAES256Util;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan(basePackages = {"com.categories", "com.member", "com.sinnake", "config", "prop"})
public class SinnakeCategoriesApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(SinnakeCategoriesApplication.class, args);
	}
	
	@Bean
	public TomcatServletWebServerFactory containerFactory() {
		TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
		
		tomcatServletWebServerFactory.addConnectorCustomizers(connector -> {
			((AbstractHttp11Protocol<?>) connector.getProtocolHandler()).setConnectionTimeout(30000);
		});
		
		return tomcatServletWebServerFactory; 
	}

}