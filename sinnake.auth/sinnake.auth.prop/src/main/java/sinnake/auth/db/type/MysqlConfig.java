package sinnake.auth.db.type;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.zaxxer.hikari.HikariDataSource;

import db.inter.DbConfigInterface;
import util.SinnakeAES256Util;


/**
 * MySQL 접속 클래스.
 * 
 * @author sinnakeWEB 
 */
@Configuration
@Component("authMySqlConfig")
public class MysqlConfig implements DbConfigInterface {
	private String userName;
	private String url;
	private String password;
	private String dbKey;
	
	/**
	 * MySQL에 접속하기 위한 접속정보 설정.
	 * 
	 * @author sinnakeWEB
	 * @param userName MySQL의 사용자 아이디
	 * @param url MySQL에 접속하기 위한 주소
	 * @param password MySQL에 접속하기 위한 비밀번호
	 * @param dbKey 암호화된 비밀번호의 복호화를 위한 복호화 키
	 */
	@Autowired
	public MysqlConfig(@Value("#{authDbProp['auth.db.mysql.username']}") String userName
		, @Value("#{authDbProp['auth.db.mysql.url']}") String url
		, @Value("#{authDbProp['auth.db.mysql.password']}") String password
		, @Value("#{authServerProp['auth.dbAes256.key']}") String dbKey) {
	
		this.userName = userName;
		this.url = url;
		this.password = password;
		this.dbKey = dbKey;
	}
	
	/**
	 * MySQL 접속 구현체
	 * @author sinnakeWEB
	 * @return {@link DataSource}
	 * @throws Exception 예외 발생.
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
