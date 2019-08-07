package com.sinnake.product.jpa;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
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
 * JPA 설정 클래스.
 * 
 * @author sinnakeWEB
 */
@Configuration
@Component("productJpaConfig")
public class JpaConfig {

	private DataSource productDataSource;

	@Autowired
	public JpaConfig(DataSource productDataSource) {
		this.productDataSource = productDataSource;
	}

	/**
	 * JPA transactionManager 객체 생성 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link JpaTransactionManager}
	 */
	@Bean("productTransactionManager")
	public JpaTransactionManager transactionManager(EntityManagerFactory productEntityManagerFactory) {
		JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
		jpaTransactionManager.setDataSource(this.productDataSource);
		jpaTransactionManager.setEntityManagerFactory(productEntityManagerFactory);

		return jpaTransactionManager;
	}
	
	/**
	 * <code>@Repository</code>에서 발생된 예외를 Spring DataAccessException으로 변환 해주기 위한 메서드.
	 * 
	 * @author sinnakeWEB
	 * @return {@link PersistenceExceptionTranslationPostProcessor}
	 */
	@Bean("productPersistenceExceptionTranslationPostProcessor")
	public PersistenceExceptionTranslationPostProcessor persistenceExceptionTranslationPostProcessor() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	/**
	 * JPA 설정을 위한 메서드(entityManagerFactory 생성).
	 * 
	 * @author sinnakeWEB
	 * @return {@link LocalContainerEntityManagerFactoryBean}
	 */
	@Bean("productEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean productEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		Properties jpaProperties = new Properties();

		localContainerEntityManagerFactoryBean.setDataSource(this.productDataSource);
		localContainerEntityManagerFactoryBean.setPackagesToScan("com.product.domain");
		localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		localContainerEntityManagerFactoryBean.setPersistenceUnitName("productEntityManager");

		jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect");
		jpaProperties.setProperty("hibernate.show_sql", "false");
		jpaProperties.setProperty("hibernate.format_sql", "true");
		jpaProperties.setProperty("hibernate.use_sql_comments", "true");
		jpaProperties.setProperty("hibernate.id.new_generator_mappings", "true");
/*		jpaProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");*/
		/*jpaProperties.setProperty("hibernate.enhancer.enableDirtyTracking", "true");*/
		/*jpaProperties.setProperty("hibernate.enhancer.enableLazyInitialization", "true");*/
		/*jpaProperties.setProperty("hibernate.enhancer.enableAssociationManagement", "true");*/
		jpaProperties.setProperty("hibernate.hbm2ddl.auto", "create");
		
	    jpaProperties.setProperty("org.hibernate.SQL", "DEBUG");
	    jpaProperties.setProperty("org.hibernate.tool.hbm2ddl", "DEBUG");
	    jpaProperties.setProperty("org.hibernate.type", "TRACE");
	    jpaProperties.setProperty("org.hibernate.stat", "DEBUG");
	    jpaProperties.setProperty("org.hibernate.type.BasicTypeRegistry", "WARN");
		
		localContainerEntityManagerFactoryBean.setJpaProperties(jpaProperties);

		return localContainerEntityManagerFactoryBean;
		
	}

}
