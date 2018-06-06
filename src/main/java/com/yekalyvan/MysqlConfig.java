package com.yekalyvan;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(basePackages = {"com.yekalyvan.*"},entityManagerFactoryRef = "db1EntityManager",
transactionManagerRef = "db1TransactionManager")//put mysql jpa repositories classes packages
public class MysqlConfig {

	@Autowired
    private Environment env;
	
	@Bean
	@Primary
	public LocalContainerEntityManagerFactoryBean db1EntityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	    em.setDataSource(db1Datasource());
	    em.setPackagesToScan(new String[]{"com.yekalyvan.*"});//put mysql model pojo classes packages
	    em.setPersistenceUnitName("db1EntityManager");
	    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
	    em.setJpaVendorAdapter(vendorAdapter);
	    HashMap<String, Object> properties = new HashMap<>();
	    properties.put("hibernate.hbm2ddl.auto",env.getProperty("spring.jpa.hibernate.ddl-auto"));
	    properties.put("hibernate.dialect", env.getProperty("spring.jpa.hibernate.dialect"));
	    properties.put("hibernate.show-sql", env.getProperty("spring.jpa.hibernate.show-sql"));
	    em.setJpaPropertyMap(properties);
	    return em;
	}
	
	@Primary
    @Bean
    public DataSource db1Datasource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.mysql.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.mysql.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.mysql.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.mysql.datasource.password"));
        return dataSource;
    }
	
	@Primary
    @Bean
    public PlatformTransactionManager db1TransactionManager() {
 
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(db1EntityManager().getObject());
        return transactionManager;
    }
	
}
