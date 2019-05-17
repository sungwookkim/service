package com.zuul.config.prop;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "tokencontrol")
public class TokenControlConfig {

	private String tokenControl = "cookie";
	
	public TokenControlConfig() { }

	public String getTokenControl() { return tokenControl; }
	public void setTokenControl(String tokenControl) { this.tokenControl = tokenControl; }
}
