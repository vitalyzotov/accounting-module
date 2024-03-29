package ru.vzotov.accounting.infrastructure.persistence.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import ru.vzotov.accounting.domain.model.*;
import ru.vzotov.cashreceipt.domain.model.PurchaseCategoryRepository;
import ru.vzotov.cashreceipt.domain.model.QRCodeRepository;
import ru.vzotov.cashreceipt.domain.model.ReceiptRepository;
import ru.vzotov.purchases.domain.model.PurchaseRepository;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration("jpa-accounting")
public class JpaConfig {

    private final DataSource dataSource;

    public JpaConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    @ConditionalOnMissingBean
    public DealRepository dealRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new DealRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public MccGroupRepository mccGroupRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new MccGroupRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public MccDetailsRepository mccDetailsRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new MccDetailsRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public BudgetRepository budgetRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new BudgetRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public BudgetPlanRepository budgetPlanRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new BudgetPlanRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public WorkCalendarRepository workCalendarRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new WorkCalendarRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public BankRepository bankRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new BankRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public CardRepository cardRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new CardRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public AccountRepository accountRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new AccountRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public BudgetCategoryRepository categoryRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new BudgetCategoryRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public OperationRepository operationRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new OperationRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public CardOperationRepository cardOperationRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new CardOperationRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public TransactionRepository transactionRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new TransactionRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public RemainRepository remainRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new RemainRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public HoldOperationRepository holdOperationRepository(
            @Qualifier("accounting-emf") EntityManager em) {
        return new HoldOperationRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public PurchaseRepository purchaseRepository(@Qualifier("accounting-emf") EntityManager em) {
        return new PurchaseRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public ReceiptRepository receiptRepository(@Qualifier("accounting-emf") EntityManager em) {
        return new ReceiptRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public QRCodeRepository qrCodeRepository(@Qualifier("accounting-emf") EntityManager em) {
        return new QRCodeRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public PurchaseCategoryRepository purchaseCategoryRepository(@Qualifier("accounting-emf") EntityManager em) {
        return new PurchaseCategoryRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public ContactRepository contactRepository(@Qualifier("accounting-emf") EntityManager em) {
        return new ContactRepositoryJpa(em);
    }

    @Bean
    @ConditionalOnMissingBean
    public PersistentPropertyRepository persistentPropertyRepository(@Qualifier("accounting-emf") EntityManager em) {
        return new PersistentPropertyRepositoryJpa(em);
    }

    @Bean("accounting-emf")
    @Qualifier("accounting-emf")
    public LocalContainerEntityManagerFactoryBean accountingEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(dataSource)
                .persistenceUnit("accounting")
                .build();
    }

    @Bean("accounting-tx")
    public JpaTransactionManager transactionManager(@Qualifier("accounting-emf") final EntityManagerFactory emf) {
        return new JpaTransactionManager(Objects.requireNonNull(emf));
    }

}
