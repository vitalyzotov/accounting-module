package ru.vzotov.accounting.interfaces.accounting;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.vzotov.accounting.interfaces.common.CommonApi;
import ru.vzotov.accounting.interfaces.purchases.PurchasesApi;

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
        public record Ref(String accountNumber) {
        }

        public record Create(String number, String name, String bankId, String currency, String owner,
                             List<String> aliases) {
        }

        public record Modify(String name, String bankId, String currency, List<String> aliases) {
        }
    }

    record AccountOperation(String account, LocalDate date, LocalDate authorizationDate,
                            String transactionReference, String operationId, String operationType, double amount,
                            String currency, String description, String comment,
                            Long categoryId) implements OperationRef {
        /**
         * @param authorizationDate not used
         */
        public record Create(LocalDate date, LocalDate authorizationDate, String transactionReference,
                             String operationType, double amount, String currency, String description,
                             String comment, Long categoryId) {
        }
    }

    record Bank(String bankId, String name, String shortName, String longName) {
        public record Create(String bankId, String name, String shortName, String longName) {
        }

        public record Modify(String name, String shortName, String longName) {
        }

        public record Ref(String bankId) {
        }
    }

    record BudgetCategory(long id, String owner, String name, String color, String icon) {
        public BudgetCategory(long id, String owner, String name) {
            this(id, owner, name, null, null);
        }

        public BudgetCategory(long id, String owner, String name, String color) {
            this(id, owner, name, color, null);
        }

        public record Ref(long id) {
        }

        public record Modify(String name, String color, String icon) {
        }

        public record Create(String name, String color, String icon) {
        }
    }

    record Budget(String budgetId, String owner, String name, String locale, String currency,
                  List<BudgetRule> rules) {
        public record Create(String name, String currency, String locale) {
        }

        public record Modify(String name, String currency, String locale) {
        }
    }

    record BudgetPlan(String itemId, String direction, String sourceAccount, String targetAccount,
                      Long categoryId, String purchaseCategoryId, CommonApi.Money value, String ruleId,
                      LocalDate date) {
    }

    record BudgetRule(String ruleId, String ruleType, String name, Long categoryId, String purchaseCategoryId,
                      String sourceAccount, String targetAccount, String recurrence, CommonApi.Money value,
                      String calculation, Boolean enabled) {
    }

    record Card(String cardNumber, String owner, LocalDate validThru, String issuer,
                List<AccountBinding> accounts) {
        public record Create(String cardNumber, YearMonth validThru, String issuer, String owner,
                             List<AccountBinding> accounts) {
        }

        public record Ref(String cardNumber) {
        }
    }

    record CardOperation(String operationId, String cardNumber, PosTerminal terminal, LocalDate authDate,
                         LocalDate purchaseDate, CommonApi.Money amount, String extraInfo,
                         String mcc) implements OperationRef {
    }

    record Deal(String dealId, String owner, LocalDate date, CommonApi.Money amount, String description, String comment,
                Long category, List<ReceiptRef> receipts, List<OperationRef> operations,
                List<OperationRef> cardOperations, List<PurchasesApi.PurchaseRef> purchases) {
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

        public record Ref(String dealId) {
        }

        public record Patch(List<String> deals) {
        }

        public record Meta(LocalDate minDate, LocalDate maxDate) {
        }
    }

    record FiscalInfo(String kktNumber, String kktRegId, String fiscalSign, String fiscalDocumentNumber,
                      String fiscalDriveNumber) implements Serializable {
    }

    record HoldOperation(String id, String account, LocalDate date, String operationType, double amount,
                         String currency, String description, String comment) {
    }

    record Item(String name, CommonApi.Money price, double quantity,
                CommonApi.Money sum, Integer index,
                String category) implements Serializable {
        public record Category(String category) {
        }
    }

    record MccDetails(String mcc, String name, String groupId) {
    }

    record MccGroup(String groupId, String name) {
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

        public record Create(String name) {
        }

        public record Modify(String name) {
        }
    }

    record QRCodeData(LocalDateTime dateTime, CommonApi.Money totalSum,
                      String fiscalDriveNumber,
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
            CommonApi.Money totalSum,
            CommonApi.Money cash,
            CommonApi.Money ecash,
            CommonApi.Money markup,
            CommonApi.Money markupSum,
            CommonApi.Money discount,
            CommonApi.Money discountSum,
            Long shiftNumber,
            String operator,
            String user,
            String userInn,
            String address,
            Long taxationType,
            Long operationType) implements ReceiptRef, Serializable {

        public Receipt(String owner, String receiptId, FiscalInfo fiscalInfo, LocalDateTime dateTime,
                       Long requestNumber, List<Item> items, List<Item> stornoItems,
                       CommonApi.Money totalSum, CommonApi.Money cash, CommonApi.Money ecash, CommonApi.Money markup, CommonApi.Money markupSum,
                       CommonApi.Money discount, CommonApi.Money discountSum, Long shiftNumber, String operator, String user,
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

        public record One(Receipt receipt) {
        }

        public record Many(List<Receipt> receipts) {
        }

        public record Register(
                @Schema(description = "Данные QR-кода чека")
                String qrcode) {
        }

        public record Ref(
                @Schema(description = "Идентификатор чека")
                String id) {
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

    record Remain(String remainId, LocalDate date, String accountNumber, CommonApi.Money value) {
        public record Create(String accountNumber, LocalDate date, CommonApi.Money value) {
        }

        public record Ref(String remainId) {
        }
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

}
