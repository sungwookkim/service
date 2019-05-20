package sinnake.auth.prop;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import prop.PropConfigAbs;

/**
 * 모든 프로퍼티를 취합하는 공통 프로퍼티 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("authPropConfig")
public class PropConfig extends PropConfigAbs {
	
	public PropConfig() {
		super("./properties/properties.xml");
	}

	/**
	 * 모든 프로퍼티를 취합하는 공통 프로퍼티를 반환 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PropertiesFactoryBean}
	 */
	@Bean("authGlobalProp")
	@Override
	public PropertiesFactoryBean globalProp() {
		return super.prop();
	}
}
