package sinnake.auth.db;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import db.DBConfigAbs;
import db.inter.DbConfigInterface;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;


/**
 * Spring DB 설정 클래스
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("authDbConfig")
@EnableTransactionManagement 
@MapperScan("com.auth.domain.service.**")
public class DBConfig extends DBConfigAbs {

	/** 
	 * @author sinnakeWEB
	 * @param mapperLocations mapper 정보
	 * @param dbConfig DB 접속 클래스
	 */
	@Autowired
	public DBConfig(@Value("#{authDbProp['auth.db.sqlSessionFactoryBean.mapperLocations']}") String mapperLocations
		, DbConfigInterface authMySqlConfig) {

		super(mapperLocations, authMySqlConfig);
	}
	    
	/**
	 * DataSoruce 설정 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link DataSource}
	 * @throws Exception 예외 발생.
	 */
    @Bean("authDataSource")
    @Override
    public DataSource dataSource() throws Exception {
    	return new Log4jdbcProxyDataSource(dbConfig.connConfig());
    }
	
    /**
     * SqlSessionFactory 생성 메서드.
     * 
     * @author sinnakeWEB
     * @return {@link DataSource}
     * @throws Exception 예외 발생.
     */
    @Bean("authSqlSessionFactoryBean")
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {		
    	SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        sessionFactory.setDataSource(dataSource());      
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
    	
        return sessionFactory;
    }   
}
