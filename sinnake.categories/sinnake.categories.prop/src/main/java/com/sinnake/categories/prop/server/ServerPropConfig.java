package com.sinnake.categories.prop.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import prop.server.ServerPropConfigAbs;

/**
 * 서버 프로퍼티 파일 설정 클래스.
 *  
 * @author sinnakeWEB
 */
@Configuration
@Component("categoriesServerPropConfig")
public class ServerPropConfig extends ServerPropConfigAbs {

	public ServerPropConfig(@Value("#{categoriesGlobalProp['categories.serverPropFile.propertie']}") String propFile
		, @Value("#{categoriesGlobalProp['categories.type.propertie']}") String serverType) {
		
		super(propFile, serverType);
	}

	/**
	 * 서버 관련 프로퍼티 반환 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PropertiesFactoryBean}
	 */
	@Bean(name = "categoriesServerProp")
	@Override
	public PropertiesFactoryBean serverProp() {
		return super.prop();
	}

}
