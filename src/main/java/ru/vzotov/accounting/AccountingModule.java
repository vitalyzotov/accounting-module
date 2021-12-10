package ru.vzotov.accounting;

import ru.vzotov.accounting.config.DatasourceConfig;
import ru.vzotov.accounting.infrastructure.persistence.jpa.JpaConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.vzotov.cashreceipt.config.ParsersConfig;

@Configuration
@Import({DatasourceConfig.class, JpaConfig.class, ParsersConfig.class})
public class AccountingModule {
}
