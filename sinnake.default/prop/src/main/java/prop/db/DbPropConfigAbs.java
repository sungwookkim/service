package prop.db;

import org.springframework.beans.factory.config.PropertiesFactoryBean;

import prop.ConfigAbs;
import prop.db.impl.MySqlInjectionEncode;
import prop.db.impl.OracleInjectionEncode;
import prop.db.inter.DbInjectionEncode;

public abstract class DbPropConfigAbs extends ConfigAbs {

	protected String propFile;
	protected String serverType;
	protected String serverDb;

	public DbPropConfigAbs(String propFile, String serverType, String serverDb) {
		super(propFile, serverType, serverDb);
	}
	
	protected DbInjectionEncode encode() {
		if("mysql".equals(serverDb)) {
			return new MySqlInjectionEncode();
		} else if("oracle".equals(serverDb)) {
			return new OracleInjectionEncode();
		}
		
		return null;		
	}
	
	public abstract PropertiesFactoryBean dbProp();
	public abstract DbInjectionEncode dbInjectionEncode();
	

}
