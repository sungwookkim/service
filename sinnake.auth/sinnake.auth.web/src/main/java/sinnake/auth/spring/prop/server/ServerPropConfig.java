package sinnake.auth.spring.prop.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * 서버 프로퍼티 파일 설정 클래스.
 *  
 * @author sinnakeWEB
 */
@Configuration
public class ServerPropConfig {

	private String propFile;
	private String serverType;
	
	@Autowired
	public ServerPropConfig(@Value("#{globalProp['serverPropFile.propertie']}") String propFile
		, @Value("#{globalProp['type.propertie']}") String serverType) {
		this.propFile = propFile;
		this.serverType = serverType;
	}
	
	/**
	 * 서버 관련 프로퍼티 반환 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PropertiesFactoryBean}
	 */
	@Bean
	public PropertiesFactoryBean serverProp() {
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource(propFile.replace("${serverType}", serverType)));
		
		return bean;
	}
}
