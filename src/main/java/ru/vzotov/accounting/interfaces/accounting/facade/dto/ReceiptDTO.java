package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public record ReceiptDTO(
        String owner,
        String receiptId,
        FiscalInfoDTO fiscalInfo,
        LocalDateTime dateTime,
        Long requestNumber,
        List<ItemDTO> items,
        List<ItemDTO> stornoItems,
        MoneyDTO totalSum,
        MoneyDTO cash,
        MoneyDTO ecash,
        MoneyDTO markup,
        MoneyDTO markupSum,
        MoneyDTO discount,
        MoneyDTO discountSum,
        Long shiftNumber,
        String operator,
        String user,
        String userInn,
        String address,
        Long taxationType,
        Long operationType) implements ReceiptRef, Serializable {

    public ReceiptDTO(String owner, String receiptId, FiscalInfoDTO fiscalInfo, LocalDateTime dateTime,
                      Long requestNumber, List<ItemDTO> items, List<ItemDTO> stornoItems,
                      MoneyDTO totalSum, MoneyDTO cash, MoneyDTO ecash, MoneyDTO markup, MoneyDTO markupSum,
                      MoneyDTO discount, MoneyDTO discountSum, Long shiftNumber, String operator, String user,
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
