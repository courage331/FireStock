package kr.co.firestock.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "kr.co.firestock.repository",
        entityManagerFactoryRef = "mydbEntityManager",
        transactionManagerRef = "mydbTransactionManager"
)
public class MyDBConfig {

    @Autowired
    private Environment env;

    private static final String prefix = "spring.mydb.datasource.hikari.";

    @Bean(name = "mydbEntityManager")
    public LocalContainerEntityManagerFactoryBean mydbEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(mydbDataSource());
        em.setPackagesToScan(new String[]{"kr.co.firestock.vo"});

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        vendorAdapter.setShowSql(true);
//        vendorAdapter.setGenerateDdl(false);
//        vendorAdapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> jpaProperties = new HashMap<>();

//        jpaProperties.put("hibernate.hbm2ddl.auto", env.getProperty("jpa.hibernate.ddl-auto"));

        em.setJpaPropertyMap(jpaProperties);

        return em;
    }

    @Bean
    public HikariDataSource mydbDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(env.getProperty(prefix + "username"));
        config.setPassword(env.getProperty(prefix + "password"));
//    	config.setDriverClassName(env.getProperty(prefix+"driverClassName"));
        config.setJdbcUrl(env.getProperty(prefix + "jdbc-url"));
        config.setMaxLifetime(Long.parseLong(env.getProperty(prefix + "max-lifetime")));
        config.setConnectionTimeout(Long.parseLong(env.getProperty(prefix + "connection-timeout")));
        config.setValidationTimeout(Long.parseLong(env.getProperty(prefix + "validation-timeout")));


        config.addDataSourceProperty("cachePrepStmts", env.getProperty(prefix + "data-source-properties.cachePrepStmts"));
        config.addDataSourceProperty("prepStmtCacheSize", env.getProperty(prefix + "data-source-properties.prepStmtCacheSize"));
        config.addDataSourceProperty("prepStmtCacheSqlLimit", env.getProperty(prefix + "data-source-properties.prepStmtCacheSqlLimit"));
        config.addDataSourceProperty("useServerPrepStmts", env.getProperty(prefix + "data-source-properties.useServerPrepStmts"));

        config = new HikariDataSource(config);
        HikariDataSource dataSource = new HikariDataSource(config);

        return dataSource;
    }

    @Bean(name = "mydbTransactionManager")
    public PlatformTransactionManager dbAdminTransactionManager() {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(mydbEntityManager().getObject());
        return transactionManager;
    }
}
