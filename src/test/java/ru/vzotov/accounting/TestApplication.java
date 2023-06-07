package ru.vzotov.accounting;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(
        scanBasePackages = {
                "ru.vzotov.accounting.test",
                "ru.vzotov.accounting.interfaces",
                "ru.vzotov.accounting.application",
                "ru.vzotov.accounting.application.impl",
                "ru.vzotov.cashreceipt.interfaces",
                "ru.vzotov.cashreceipt.application",
                "ru.vzotov.cashreceipt.application.impl"
        }
)
@EnableTransactionManagement
@EnableAspectJAutoProxy
@EnableMethodSecurity(securedEnabled = true)
@Import({AccountingModule.class})
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("accounting-emf") LocalContainerEntityManagerFactoryBean bean) {
        return bean;
    }
}
