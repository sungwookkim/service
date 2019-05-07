package com.zuul.config.prop;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 모든 프로퍼티를 취합하는 공통 프로퍼티 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
public class PropConfig {
	private static String globalPropFile = "./properties/properties.xml";
	
	/**
	 * 모든 프로퍼티를 취합하는 공통 프로퍼티를 반환 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PropertiesFactoryBean}
	 */
	@Bean
	public PropertiesFactoryBean globalProp() {
	    PropertiesFactoryBean bean = new PropertiesFactoryBean();	    
	    bean.setLocation(new ClassPathResource(globalPropFile));

	    return bean;
	}
}
