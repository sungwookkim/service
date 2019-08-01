package com.sinnake.prop.prop.db;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import prop.db.DbPropConfigAbs;
import prop.db.inter.DbInjectionEncode;

/**
 * DB 프로퍼티 파일 설정 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("temporaryDbPropConfig")
public class DbPropConfig extends DbPropConfigAbs {

	public DbPropConfig(@Value("#{temporaryGlobalProp['temporary.dbPropFile.propertie']}") String propFile
		, @Value("#{temporaryGlobalProp['temporary.type.propertie']}") String serverType
		, @Value("#{temporaryGlobalProp['temporary.dbType.propertie']}") String serverDb) {

		super(propFile, serverType, serverDb);
	}

	/**
	 * DB와 관련된 프로퍼티를 반환하는 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PropertiesFactoryBean}
	 */
	@Bean("temporaryDbProp")
	@Override
	public PropertiesFactoryBean dbProp() {
		return super.prop();
	}

	/**
	 * DB별 Injection 처리 클래스 반환하는 메소드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link DbInjectionEncode}
	 */
	@Bean("temporaryDbInjectionEncode")
	@Override
	public DbInjectionEncode dbInjectionEncode() {
		return super.encode();
	}	

}
