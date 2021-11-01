package ru.vzotov.accounting.config;

import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration("datasource-accounting")
public class DatasourceConfig {

    private final DataSource dataSource;

    public DatasourceConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @ConfigurationProperties("accounting.liquibase")
    @Accounting
    public LiquibaseProperties accountingLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean
    @Accounting
    public SpringLiquibase accountingLiquibase() {
        return springLiquibase(dataSource, accountingLiquibaseProperties());
    }

    private static SpringLiquibase springLiquibase(DataSource dataSource, LiquibaseProperties properties) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog(properties.getChangeLog());
        liquibase.setContexts(properties.getContexts());
        liquibase.setDefaultSchema(properties.getDefaultSchema());
        liquibase.setDropFirst(properties.isDropFirst());
        liquibase.setShouldRun(properties.isEnabled());
        liquibase.setLabels(properties.getLabels());
        liquibase.setChangeLogParameters(properties.getParameters());
        liquibase.setRollbackFile(properties.getRollbackFile());
        return liquibase;
    }
}
