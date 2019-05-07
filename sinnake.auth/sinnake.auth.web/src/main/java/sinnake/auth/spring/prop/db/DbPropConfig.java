package sinnake.auth.spring.prop.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import sinnake.auth.spring.prop.db.impl.MySqlInjectionEncode;
import sinnake.auth.spring.prop.db.impl.OracleInjectionEncode;
import sinnake.auth.spring.prop.db.inter.DbInjectionEncode;

/**
 * DB 프로퍼티 파일 설정 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("dbPropConfig")
public class DbPropConfig {	

	private String propFile;
	private String serverType;
	private String serverDb;

	@Autowired
	public DbPropConfig(@Value("#{globalProp['dbPropFile.propertie']}") String propFile
		, @Value("#{globalProp['type.propertie']}") String serverType
		, @Value("#{globalProp['dbType.propertie']}") String serverDb) {

		this.propFile = propFile;
		this.serverType = serverType;
		this.serverDb = serverDb;
	}

	/**
	 * DB와 관련된 프로퍼티를 반환하는 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PropertiesFactoryBean}
	 */
	@Bean
	public PropertiesFactoryBean dbProp() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource(propFile.replace("${serverType}", serverType).replace("${serverDb}", serverDb)));
		
		return bean;
	}
	
	/**
	 * DB별 Injection 처리 클래스 반환하는 메소드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link DbInjectionEncode}
	 */
	@Bean
	public DbInjectionEncode dbInjectionEncode() {
		if("mysql".equals(serverDb)) {
			return new MySqlInjectionEncode();
		} else if("oracle".equals(serverDb)) {
			return new OracleInjectionEncode();
		}
		
		return null;
	}
}
