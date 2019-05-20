package prop;

import java.util.Optional;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

public abstract class ConfigAbs {

	protected String propFile;
	protected String serverType;
	protected String serverDb;

	public ConfigAbs(String propFile, String serverType) {
		
		this.propFile = propFile;
		this.serverType = serverType;
	}
	
	public ConfigAbs(String propFile, String serverType, String serverDb) {
	
		this.propFile = propFile;
		this.serverType = serverType;
		this.serverDb = serverDb;
	}
	
	protected PropertiesFactoryBean prop() {
		String path = Optional.ofNullable(propFile.replace("${serverType}", serverType))
			.map(st -> {
				return Optional.ofNullable(serverDb)
					.map(sd -> st.replace("${serverDb}", sd))
					.orElse(st);								
			})
		.get();
		
		PropertiesFactoryBean bean = new PropertiesFactoryBean();
		bean.setLocation(new ClassPathResource(path));
		
		return bean;
	}	
}
