package sinnake.auth.prop.db;

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
@Component("authDbPropConfig")
public class DbPropConfig extends DbPropConfigAbs {

	public DbPropConfig(@Value("#{authGlobalProp['auth.dbPropFile.propertie']}") String propFile
		, @Value("#{authGlobalProp['auth.type.propertie']}") String serverType
		, @Value("#{authGlobalProp['auth.dbType.propertie']}") String serverDb) {
		
		super(propFile, serverType, serverDb);
	}

	@Bean("authDbProp")
	@Override
	public PropertiesFactoryBean dbProp() {
		return super.prop();
	}

	@Bean("authDbInjectionEncode")
	@Override
	public DbInjectionEncode dbInjectionEncode() {
		return super.encode();
	}	

}
