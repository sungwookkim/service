package com.sinnake.product.prop;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import prop.PropConfigAbs;

@Configuration
@Component("webPropConfig")
public class PropConfig extends PropConfigAbs {
	
	public PropConfig() {
		super("./properties/web_properties.xml");
	}
	
	@Bean("webGlobalProp")
	@Override
	public PropertiesFactoryBean globalProp() {
		return super.prop();
	}

}
