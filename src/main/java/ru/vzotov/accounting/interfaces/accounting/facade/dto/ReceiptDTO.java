package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public final class ReceiptDTO implements Serializable {
    private String checkId;
    private FiscalInfoDTO fiscalInfo;
    private LocalDateTime dateTime;
    private Long requestNumber;
    private List<ItemDTO> items;
    private List<ItemDTO> stornoItems;
    private MoneyDTO totalSum;
    private MoneyDTO cash;
    private MoneyDTO ecash;
    private MoneyDTO markup;
    private MoneyDTO markupSum;
    private MoneyDTO discount;
    private MoneyDTO discountSum;

    private Long shiftNumber;
    private String operator;

    private String user;
    private String userInn;
    private String address;
    private Long taxationType;

    private Long operationType;

    public ReceiptDTO() {
    }

    public ReceiptDTO(String checkId,
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
                      long operationType) {
        this.checkId = checkId;
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

    public List<ItemDTO> getStornoItems() {
        return stornoItems;
    }

    public void setStornoItems(List<ItemDTO> stornoItems) {
        this.stornoItems = stornoItems;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public void setFiscalInfo(FiscalInfoDTO fiscalInfo) {
        this.fiscalInfo = fiscalInfo;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public void setTotalSum(MoneyDTO totalSum) {
        this.totalSum = totalSum;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserInn() {
        return userInn;
    }

    public void setUserInn(String userInn) {
        this.userInn = userInn;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getTaxationType() {
        return taxationType;
    }

    public void setTaxationType(Long taxationType) {
        this.taxationType = taxationType;
    }

    public Long getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Long shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public MoneyDTO getMarkup() {
        return markup;
    }

    public void setMarkup(MoneyDTO markup) {
        this.markup = markup;
    }

    public MoneyDTO getMarkupSum() {
        return markupSum;
    }

    public void setMarkupSum(MoneyDTO markupSum) {
        this.markupSum = markupSum;
    }

    public MoneyDTO getDiscount() {
        return discount;
    }

    public void setDiscount(MoneyDTO discount) {
        this.discount = discount;
    }

    public MoneyDTO getDiscountSum() {
        return discountSum;
    }

    public void setDiscountSum(MoneyDTO discountSum) {
        this.discountSum = discountSum;
    }

    public Long getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Long requestNumber) {
        this.requestNumber = requestNumber;
    }

    public String getCheckId() {
        return checkId;
    }

    public FiscalInfoDTO getFiscalInfo() {
        return fiscalInfo;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public MoneyDTO getTotalSum() {
        return totalSum;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }

    public MoneyDTO getCash() {
        return cash;
    }

    public void setCash(MoneyDTO cash) {
        this.cash = cash;
    }

    public MoneyDTO getEcash() {
        return ecash;
    }

    public void setEcash(MoneyDTO ecash) {
        this.ecash = ecash;
    }
}
