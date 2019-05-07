package com.sinnake.member.web.spring.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.sinnake.member.web.spring.db.inter.DbConfigInterface;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

/**
 * DB 설정 클래스
 * 
 * @author sinnakeWEB
 */
@Configuration
@EnableTransactionManagement 
public class DBConfig {

	private DbConfigInterface dbConfig;

	@Autowired
	public DBConfig(DbConfigInterface dbConfig) {
		this.dbConfig = dbConfig;
	}
	
	/**
	 * DataSoruce 객체 반환
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
    public DataSource dataSource() throws Exception {
    	DataSource dataSource = dbConfig.connConfig();

    	return new Log4jdbcProxyDataSource(dataSource);
    }
}
