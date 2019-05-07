package com.sinnake.member.web.spring.db.type;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.sinnake.member.web.spring.db.inter.DbConfigInterface;
import com.zaxxer.hikari.HikariDataSource;

import util.SinnakeAES256Util;

/**
 * MsSQL 접속 정보 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("dbConfig")
public class MysqlConfig implements DbConfigInterface {
	private String userName;
	private String url;
	private String password;
	private String dbKey;
	
	@Autowired
	public MysqlConfig(@Value("#{dbProp['db.mysql.username']}") String userName
		, @Value("#{dbProp['db.mysql.url']}") String url
		, @Value("#{dbProp['db.mysql.password']}") String password
		, @Value("#{serverProp['dbAes256.key']}") String dbKey) {
		this.userName = userName;
		this.url = url;
		this.password = password;
		this.dbKey = dbKey;
	}

	/**
	 * mssql 접속 정보 반환 메서드
	 * 
	 * @author sinnakeWEB
	 */
	public DataSource connConfig() throws Exception {
		SinnakeAES256Util aes256 = new SinnakeAES256Util(dbKey);

		Properties connectionProperties = new Properties(); 
		connectionProperties.put("autoCommit", "false");
		connectionProperties.put("autoReconnection", "true");
		connectionProperties.put("useSSL", "false");
		connectionProperties.put("serverTimezone", "UTC");
		connectionProperties.put("cachePrepStmts", "true");
		connectionProperties.put("prepStmtCacheSize", "250");
		connectionProperties.put("prepStmtCacheSqlLimit", "2048");
		connectionProperties.put("useServerPrepStmts", "true");
		connectionProperties.put("useLocalSessionState", "true");
		connectionProperties.put("rewriteBatchedStatements", "true");
		connectionProperties.put("cacheResultSetMetadata", "true");
		connectionProperties.put("cacheServerConfiguration", "true");
		connectionProperties.put("elideSetAutoCommits", "true");
		connectionProperties.put("maintainTimeStats", "false");
		
		HikariDataSource hikariDataSource = new HikariDataSource();
		hikariDataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
		hikariDataSource.setUsername(aes256.aesDecode(userName));
		hikariDataSource.setJdbcUrl(aes256.aesDecode(url));
		hikariDataSource.setPassword(aes256.aesDecode(password));
		hikariDataSource.setDataSourceProperties(connectionProperties);

        return hikariDataSource;
	}
}
