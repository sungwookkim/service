package prop.server;

import org.springframework.beans.factory.config.PropertiesFactoryBean;

import prop.ConfigAbs;

public abstract class ServerPropConfigAbs extends ConfigAbs {

	public ServerPropConfigAbs(String propFile, String serverType) {
		super(propFile, serverType);
	}

	public abstract PropertiesFactoryBean serverProp();
}
