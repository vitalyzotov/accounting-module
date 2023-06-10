package ru.vzotov.tools;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ru.vzotov.accounting.AccountingModule;
import ru.vzotov.cashreceipt.application.nalogru2.ReceiptRepositoryNalogru2;
import ru.vzotov.cashreceipt.domain.model.QRCodeData;

@SpringBootApplication(
        scanBasePackages = {
                "ru.vzotov.cashreceipt.application",
                "ru.vzotov.cashreceipt.application.impl"}
)
@EnableTransactionManagement
@EnableAspectJAutoProxy
@Import(AccountingModule.class)
public class Nalogru2Tool implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Nalogru2Tool.class);

    @Autowired
    private ReceiptRepositoryNalogru2 repository;

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return objectMapper;
    }

    public static void main(String... args) throws Exception {
        SpringApplication.run(Nalogru2Tool.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("EXECUTING : command line runner");

        for (int i = 0; i < args.length; ++i) {
            log.info("args[{}]: {}", i, args[i]);
        }

        QRCodeData data = new QRCodeData("t=20200805T2111&s=3405.69&fn=9285000100152864&i=135055&fp=1196097479&n=1");
        String ticket = repository.findByQRCodeData(data);
        log.info(ticket);
    }
}
