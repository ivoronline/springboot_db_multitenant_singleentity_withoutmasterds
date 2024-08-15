package com.ivoronline.multitenant.tenant.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
  basePackages            = "com.ivoronline.multitenant.tenant.repository",
  entityManagerFactoryRef = "tenantEntityManager",
  transactionManagerRef   = "tenantTransactionManager"
)
public class TenantConfig {

  //PROPERTIES
  public static Map<Object, Object> targetDataSources = new HashMap<>();
  public static TenantDataSource    tenantDataSource  = new TenantDataSource();
  private final String              ENTITY_PACKAGE    = "com.ivoronline.multitenant.tenant.entity";
  
  //=========================================================================================================
  // DEFAULT DATA SOURCE
  //=========================================================================================================
  @Primary
  @Lazy
  @Bean
  @ConfigurationProperties("default.spring.datasource")
  public DataSource defaultDataSource() {
    return DataSourceBuilder.create().type(HikariDataSource.class).build();
  }
  
  //=========================================================================================================
  // TENANT DATA SOURCE
  //=========================================================================================================
  @Bean
  public TenantDataSource tenantDataSource() {
    tenantDataSource.setDefaultTargetDataSource(defaultDataSource());
    tenantDataSource.setTargetDataSources(targetDataSources);
    return tenantDataSource;
  }
  
  //=========================================================================================================
  // ENTITY MANAGER FACTORY BEAN
  //=========================================================================================================
  @Bean
  public LocalContainerEntityManagerFactoryBean tenantEntityManager() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
                                           em.setDataSource(tenantDataSource());
                                           em.setPackagesToScan(ENTITY_PACKAGE);
                                           em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
                                           em.setJpaProperties(hibernateProperties());
    return em;
  }
  
  //=========================================================================================================
  // TRANSACTION MANAGER
  //=========================================================================================================
  @Bean
  public PlatformTransactionManager tenantTransactionManager() {
    JpaTransactionManager transactionManager = new JpaTransactionManager();
                          transactionManager.setEntityManagerFactory(tenantEntityManager().getObject());
    return transactionManager;
  }
  
  //=========================================================================================================
  // SESSION FACTORY
  //=========================================================================================================
  @Primary
  @Bean
  public LocalSessionFactoryBean dbSessionFactory() {
    LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
                            sessionFactoryBean.setDataSource(tenantDataSource());
                            sessionFactoryBean.setPackagesToScan(ENTITY_PACKAGE);
                            sessionFactoryBean.setHibernateProperties(hibernateProperties());
    return sessionFactoryBean;
  }
  
  //=========================================================================================================
  // HIBERNATE PROPERTIES
  //=========================================================================================================
  private Properties hibernateProperties() {
    Properties properties = new Properties();
               properties.put("hibernate.show_sql"  , true);
               properties.put("hibernate.format_sql", true);
    return properties;
  }
  
}