package ru.vzotov.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
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
@Import({AccountingModule.class})
public class TestApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

}
