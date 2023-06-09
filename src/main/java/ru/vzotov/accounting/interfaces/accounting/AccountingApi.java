package ru.vzotov.accounting.interfaces.accounting;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.vzotov.accounting.interfaces.purchases.facade.dto.PurchaseRef;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

public interface AccountingApi {

    record AccountBinding(String accountNumber, LocalDate from, LocalDate to) {
    }

    record Account(String number, String name, String bankId, String currency, String owner,
                   List<String> aliases) implements Serializable {
    }

    record AccountOperation(String account, LocalDate date, LocalDate authorizationDate,
                            String transactionReference, String operationId, String operationType, double amount,
                            String currency, String description, String comment,
                            Long categoryId) implements OperationRef {
    }

    record Bank(String bankId, String name, String shortName, String longName) {
    }

    record BudgetCategory(long id, String owner, String name, String color, String icon) {
        public BudgetCategory(long id, String owner, String name) {
            this(id, owner, name, null, null);
        }

        public BudgetCategory(long id, String owner, String name, String color) {
            this(id, owner, name, color, null);
        }

    }

    record Budget(String budgetId, String owner, String name, String locale, String currency,
                  List<BudgetRule> rules) {
    }

    record BudgetPlan(String itemId, String direction, String sourceAccount, String targetAccount,
                      Long categoryId, String purchaseCategoryId, Money value, String ruleId, LocalDate date) {
    }

    record BudgetRule(String ruleId, String ruleType, String name, Long categoryId, String purchaseCategoryId,
                      String sourceAccount, String targetAccount, String recurrence, Money value,
                      String calculation, Boolean enabled) {
    }

    record Card(String cardNumber, String owner, LocalDate validThru, String issuer,
                List<AccountBinding> accounts) {
    }

    record CardOperation(String operationId, String cardNumber, PosTerminal terminal, LocalDate authDate,
                         LocalDate purchaseDate, Money amount, String extraInfo, String mcc) implements OperationRef {
    }

    record Deal(String dealId, String owner, LocalDate date, Money amount, String description, String comment,
                Long category, List<ReceiptRef> receipts, List<OperationRef> operations,
                List<OperationRef> cardOperations, List<PurchaseRef> purchases) {
        public enum Expansion {
            RECEIPTS("receipts"),
            OPERATIONS("operations"),
            CARD_OPERATIONS("card_operations"),
            PURCHASES("purchases");

            private final String value;

            Expansion(String value) {
                this.value = value;
            }

            public static Expansion of(String value) {
                for (Expansion opt : Expansion.values()) {
                    if (opt.value.equalsIgnoreCase(value)) return opt;
                }
                throw new IllegalArgumentException("Unknown expansion");
            }

        }
    }

    record FiscalInfo(String kktNumber, String kktRegId, String fiscalSign, String fiscalDocumentNumber,
                      String fiscalDriveNumber) implements Serializable {
    }

    record HoldOperation(String id, String account, LocalDate date, String operationType, double amount,
                         String currency, String description, String comment) {
    }

    record Item(String name, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO price, double quantity, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO sum, Integer index,
                String category) implements Serializable {
    }

    record MccDetails(String mcc, String name, String groupId) {
    }

    record MccGroup(String groupId, String name) {
    }

    record Money(long amount, String currency) implements Serializable {
    }

    record OperationId(String operationId) implements OperationRef {
    }

    @JsonAutoDetect(fieldVisibility = ANY)
    @JsonTypeInfo(use = DEDUCTION, defaultImpl = OperationId.class)
    @JsonSubTypes({@JsonSubTypes.Type(OperationId.class), @JsonSubTypes.Type(AccountOperation.class), @JsonSubTypes.Type(CardOperation.class)})
    interface OperationRef {
         String operationId();
    }

    record PosTerminal(String terminalId, String country, String city, String street, String merchant) {
    }

    record PurchaseCategory(String categoryId, String owner, String name, String parentId) {
        public PurchaseCategory(String categoryId, String owner, String name) {
            this(categoryId, owner, name, null);
        }
    }

    record QRCodeData(LocalDateTime dateTime, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO totalSum, String fiscalDriveNumber,
                      String fiscalDocumentNumber, String fiscalSign, Long operationType) {
    }

    record QRCode(String receiptId, QRCodeData data, String state, Long loadingTryCount,
                  OffsetDateTime loadedAt, String owner) implements ReceiptRef {
    }

    record Receipt(
            String owner,
            String receiptId,
            FiscalInfo fiscalInfo,
            LocalDateTime dateTime,
            Long requestNumber,
            List<Item> items,
            List<Item> stornoItems,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO totalSum,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO cash,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO ecash,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO markup,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO markupSum,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO discount,
            ru.vzotov.accounting.interfaces.common.dto.MoneyDTO discountSum,
            Long shiftNumber,
            String operator,
            String user,
            String userInn,
            String address,
            Long taxationType,
            Long operationType) implements ReceiptRef, Serializable {

        public Receipt(String owner, String receiptId, FiscalInfo fiscalInfo, LocalDateTime dateTime,
                       Long requestNumber, List<Item> items, List<Item> stornoItems,
                       ru.vzotov.accounting.interfaces.common.dto.MoneyDTO totalSum, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO cash, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO ecash, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO markup, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO markupSum,
                       ru.vzotov.accounting.interfaces.common.dto.MoneyDTO discount, ru.vzotov.accounting.interfaces.common.dto.MoneyDTO discountSum, Long shiftNumber, String operator, String user,
                       String userInn, String address, Long taxationType, Long operationType) {
            this.owner = owner;
            this.receiptId = receiptId;
            this.fiscalInfo = fiscalInfo;
            this.dateTime = dateTime;
            this.requestNumber = requestNumber;
            this.items = Collections.unmodifiableList(items);
            this.stornoItems = Collections.unmodifiableList(stornoItems);
            this.totalSum = totalSum;
            this.cash = cash;
            this.ecash = ecash;
            this.markup = markup;
            this.markupSum = markupSum;
            this.discount = discount;
            this.discountSum = discountSum;
            this.shiftNumber = shiftNumber;
            this.operator = operator;
            this.user = user;
            this.userInn = userInn;
            this.address = address;
            this.taxationType = taxationType;
            this.operationType = operationType;
        }
    }

    record ReceiptId(String receiptId) implements ReceiptRef {
    }

    @JsonAutoDetect(fieldVisibility = ANY)
    @JsonTypeInfo(use = DEDUCTION, defaultImpl = ReceiptId.class)
    @JsonSubTypes({@JsonSubTypes.Type(ReceiptId.class), @JsonSubTypes.Type(Receipt.class), @JsonSubTypes.Type(QRCode.class)})
    interface ReceiptRef {
        String receiptId();
    }

    record Remain(String remainId, LocalDate date, String accountNumber, Money value) {
    }

    record SpecialDay(LocalDate day, String name, String type) {
    }

    record Timeline(List<TimePeriod> periods) {
        public Timeline() {
            this(new ArrayList<>());
        }
    }

    record TimePeriod(LocalDate fromDay, LocalDate toDay, long count) {
    }

    record Transaction(String primary, String secondary) {
    }

    record WorkCalendar(LocalDate from, LocalDate to, String location, List<SpecialDay> specialDays) {
    }

    record AccountCreateRequest(String number, String name, String bankId, String currency, String owner,
                                       List<String> aliases) {

    }

    record AccountModifyRequest(String name, String bankId, String currency, List<String> aliases) {

    }

    record AccountStoreResponse(String accountNumber) {

    }

    record BankCreateRequest(String bankId, String name, String shortName, String longName) {
    }

    record BankStoreResponse(String bankId) {

    }

    record BudgetCategoryCreateRequest(String name, String color, String icon) {

    }

    record BudgetCategoryDeleteResponse(long id) {

    }

    record BudgetCategoryModifyRequest(String name, String color, String icon) {

    }

    record BudgetCreateRequest(String name, String currency, String locale) {

    }

    record BudgetModifyRequest(String name, String currency, String locale) {
    }

    record CardCreateRequest(String cardNumber, YearMonth validThru, String issuer, String owner,
                             List<AccountBinding> accounts) {
    }

    record CardStoreResponse(String cardNumber) {
    }

    record DealReference(String dealId) {
    }

    record DealsMetadataResponse(LocalDate minDate, LocalDate maxDate) {
    }

    record GetReceiptResponse(Receipt receipt) {

    }

    record GetReceiptsResponse(List<Receipt> receipts) {

    }

    record NalogPreAuthRequest(String sessionId, String refreshToken) {
    }

    /**
     * @param authorizationDate not used
     */
    record OperationCreateRequest(LocalDate date, LocalDate authorizationDate, String transactionReference,
                                         String operationType, double amount, String currency, String description,
                                         String comment, Long categoryId) {
    }

    record PatchDealsRequest(List<String> deals) {
    }

    record ReceiptItemCategoryPatch(String category) {
    }

    record ReceiptRegistrationRequest(String qrcode) {
    }

    record ReceiptRegistrationResponse(String id) {
    }

    record RemainCreateRequest(String accountNumber, LocalDate date, Money value) {
    }

    record RemainCreateResponse(String remainId) {
    }

    record RemainDeleteResponse(String remainId) {
    }
}
