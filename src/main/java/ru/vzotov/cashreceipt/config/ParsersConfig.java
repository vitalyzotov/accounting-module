package ru.vzotov.cashreceipt.config;

import ru.vzotov.cashreceipt.application.ReceiptParsingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vzotov.cashreceipt.application.nalogru.ReceiptParsingServiceImpl;

@Configuration
public class ParsersConfig {

    @Bean
    public ReceiptParsingService nalogruParser() {
        return new ReceiptParsingServiceImpl();
    }

    @Bean
    public ReceiptParsingService nalogru2Parser() {
        return new ru.vzotov.cashreceipt.application.nalogru2.ReceiptParsingServiceImpl();
    }
}
