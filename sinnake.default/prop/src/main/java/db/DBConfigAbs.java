package db;

import javax.sql.DataSource;

import db.inter.DbConfigInterface;

public abstract class DBConfigAbs {

	protected DbConfigInterface dbConfig;
	protected String mapperLocations;
	
	public DBConfigAbs(DbConfigInterface dbConfig) {
		this.dbConfig = dbConfig;
		this.mapperLocations = "";
	}

	public DBConfigAbs(String mapperLocations, DbConfigInterface dbConfig) {
		this.dbConfig = dbConfig;
		this.mapperLocations = mapperLocations;
	}
	
	public abstract DataSource dataSource() throws Exception;
}
