package sinnake.auth.jpa;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Component;

/**
 * JPA 설정 클래스
 *  
 * @author sinnakeWEB
 */
@Configuration
@Component("authJpaConfig")
public class JpaConfig {

	private DataSource authDataSource;

	@Autowired
	public JpaConfig(DataSource authDataSource) {
		this.authDataSource = authDataSource;
	}
	
	/**
	 * JPA transactionManager 객체 생성 메서드.
	 * 
	 * @return {@link JpaTransactionManager}
	 */
	@Bean("authTransactionManager")
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setDataSource(authDataSource);
		
		return jpaTransactionManager;
	}
	
	/**
	 * <code>@Repository</code>에서 발생된 예외를 Spring DataAccessException으로 변환 해주기 위한 메서드.
	 * 
	 * @return {@link PersistenceExceptionTranslationPostProcessor}
	 */
	@Bean("authPersistenceExceptionTranslationPostProcessor")
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * JPA 설정을 위한 메서드(entityManagerFactory 생성).
	 * 
	 * @return {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean("authEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		Properties jpaProperties = new Properties();

		localContainerEntityManagerFactoryBean.setDataSource(this.authDataSource);
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.auth.domain");
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		
		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		jpaProperties.setProperty("hibernate.show_sql", "false");
		jpaProperties.setProperty("hibernate.format_sql", "true");
		jpaProperties.setProperty("hibernate.use_sql_comments", "true");
		jpaProperties.setProperty("hibernate.id.new_generator_mappings", "true");
/*		jpaProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");*/
		/*jpaProperties.setProperty("hibernate.enhancer.enableDirtyTracking", "true");*/
		/*jpaProperties.setProperty("hibernate.enhancer.enableLazyInitialization", "true");*/
		/*jpaProperties.setProperty("hibernate.enhancer.enableAssociationManagement", "true");*/
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
		
	    jpaProperties.setProperty("org.hibernate.SQL", "DEBUG");
	    jpaProperties.setProperty("org.hibernate.tool.hbm2ddl", "DEBUG");
	    jpaProperties.setProperty("org.hibernate.type", "TRACE");
	    jpaProperties.setProperty("org.hibernate.stat", "DEBUG");
	    jpaProperties.setProperty("org.hibernate.type.BasicTypeRegistry", "WARN");
		
		localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);

		return localContainerEntityManagerFactoryBean;
		
	}

}
