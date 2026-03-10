package com.knubisoft.testapi.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@PropertySource({"classpath:application-local.properties"})
@EnableJpaRepositories(
        basePackages = "com.knubisoft.testapi.repository.postgresUsers",
        entityManagerFactoryRef = "postgresUsersEntityManager",
        transactionManagerRef = "postgresUsersTransactionManager")
@ConditionalOnExpression("${jwt.postgres.users.enabled:true} || ${basic.postgres.users.enabled:true}")
public class PostgresUsersConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean postgresUsersEntityManager(final Environment env) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(postgresUsersDataSource());
        em.setPackagesToScan("com.knubisoft.testapi.model.postgresUsers");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("postgres.users.datasource.hibernate.hbm2ddl.auto"));
        properties.put("hibernate.dialect", env.getProperty("postgres.users.datasource.hibernate.dialect"));
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean
    @ConfigurationProperties(prefix = "postgres.users.datasource")
    public DataSource postgresUsersDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public PlatformTransactionManager postgresUsersTransactionManager(final Environment env) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(postgresUsersEntityManager(env).getObject());
        return transactionManager;
    }
}
