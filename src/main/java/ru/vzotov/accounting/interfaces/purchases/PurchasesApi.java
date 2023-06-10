package ru.vzotov.accounting.interfaces.purchases;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.vzotov.accounting.interfaces.common.CommonApi;

import java.time.LocalDateTime;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.DEDUCTION;

public interface PurchasesApi {

    record Purchase(String purchaseId, String owner, String receiptId, String name, LocalDateTime dateTime,
                    CommonApi.Money price, Double quantity, String categoryId) implements PurchaseRef {
        public record Create(String dealId, List<Data> purchases) {

        }

        public record Ref(String purchaseId) {

        }

        public record Refs(List<String> purchaseId) {

        }

        public record Data(
                String name,
                LocalDateTime dateTime,
                CommonApi.Money price,
                Double quantity,
                String receiptId,
                String categoryId) {

            public Data(String name, LocalDateTime dateTime, CommonApi.Money price, Double quantity) {
                this(name, dateTime, price, quantity, null, null);
            }
        }
    }

    record PurchaseId(String purchaseId) implements PurchaseRef {
    }

    @JsonAutoDetect(fieldVisibility = ANY)
    @JsonTypeInfo(use = DEDUCTION, defaultImpl = PurchaseId.class)
    @JsonSubTypes({@JsonSubTypes.Type(PurchaseId.class), @JsonSubTypes.Type(Purchase.class)})
    interface PurchaseRef {
        String purchaseId();
    }

}
