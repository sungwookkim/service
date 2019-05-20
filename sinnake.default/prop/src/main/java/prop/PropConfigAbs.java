package prop;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

public abstract class PropConfigAbs {

	protected String propFile;
	
	public PropConfigAbs(String propFile) {
		this.propFile = propFile;
	}
	
	protected PropertiesFactoryBean prop() {
	    PropertiesFactoryBean bean = new PropertiesFactoryBean();	    
	    bean.setLocation(new ClassPathResource(this.propFile));

	    return bean;
	}
	
	public abstract PropertiesFactoryBean globalProp();
}
