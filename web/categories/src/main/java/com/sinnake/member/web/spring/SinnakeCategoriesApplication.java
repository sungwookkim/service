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
		try {
			new SinnakeAES256Util("sinnakeAes256@$%DB").aesDecode("MnNNNxK6gwZo9c9Jv8s33g==");
			new SinnakeAES256Util("sinnakeAes256@$%DB").aesDecode("ytHp9z9rxLogkLtiOcJx/A==");
			new SinnakeAES256Util("sinnakeAes256@$%DB").aesDecode("DB5WQK4FSTP9fowhoQHkX8chftf4sKCEeUVlxYeGpM11AK6ESqaU1BF1FPm3KvnHAVY7sa2RhslyVLGU0fG7hwZyaXv5jEzp6/dLce1GCSk=");
		} catch (Exception e) {
			// TODO: handle exception
		}
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