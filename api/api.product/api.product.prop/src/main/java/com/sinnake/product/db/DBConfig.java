package com.sinnake.product.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import db.DBConfigAbs;
import db.inter.DbConfigInterface;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;

/**
 * DB 설정 클래스
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("productDbConfig")
@EnableTransactionManagement 
public class DBConfig extends DBConfigAbs {

	@Autowired
	public DBConfig(DbConfigInterface productMySqlConfig) {
		super(productMySqlConfig);
	}

	/**
	 * DataSoruce 객체 반환
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean("productDataSource")
	@Override
	public DataSource dataSource() throws Exception {
		DataSource dataSource = dbConfig.connConfig();

    	return new Log4jdbcProxyDataSource(dataSource);
	}
}
