package sinnake.auth.spring.db;

import javax.sql.DataSource;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import sinnake.auth.spring.db.inter.DbConfigInterface;


/**
 * Spring DB 설정 클래스
 * 
 * @author sinnakeWEB
 */
@Configuration
@EnableTransactionManagement 
@MapperScan("com.auth.domain.service.**")
public class DBConfig {

	private String mapperLocations;		
	private DbConfigInterface dbConfig;

	/** 
	 * @author sinnakeWEB
	 * @param mapperLocations mapper 정보
	 * @param dbConfig DB 접속 클래스
	 */
	@Autowired
	public DBConfig(@Value("#{dbProp['db.sqlSessionFactoryBean.mapperLocations']}") String mapperLocations
		, DbConfigInterface dbConfig) {
		this.mapperLocations = mapperLocations;
		this.dbConfig = dbConfig;
	}
	
    /*

     */
	/**
	 * DataSoruce 설정 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link DataSource}
	 * @throws Exception 예외 발생.
	 */
    @Bean
    public DataSource log4jdbcDataSource() throws Exception {
    	return new Log4jdbcProxyDataSource(dbConfig.connConfig());
    }
	
    /**
     * SqlSessionFactory 생성 메서드.
     * 
     * @author sinnakeWEB
     * @return {@link DataSource}
     * @throws Exception 예외 발생.
     */
    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception {		
    	SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();

        sessionFactory.setDataSource(log4jdbcDataSource());      
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
    	
        return sessionFactory;
    }
}
