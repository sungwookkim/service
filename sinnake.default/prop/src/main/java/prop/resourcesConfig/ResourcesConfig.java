package prop.resourcesConfig;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

public interface ResourcesConfig {
	
	public void configure(HttpSecurity http) throws Exception;
}
