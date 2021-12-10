package ru.vzotov.cashreceipt.application.nalogru2;

import java.time.Instant;
import java.util.List;

@SuppressWarnings("unused")
class Receipt {

    String kktRegId;

    Long protocolVersion;

    Long messageFiscalSign;

    Long internetSign;

    Long fiscalDocumentFormatVer;

    Long nds18;

    Long taxationType;

    String retailPlaceAddress;

    Long fiscalDocumentNumber;

    String operator;

    Instant dateTime; //1596782760

    Object message;

    String rawData; // base64 encoded string

    Long ecashTotalSum;

    String fiscalDriveNumber; // "8710000100313204"

    Long shiftNumber;

    String senderAddress;

    String user;
    String userInn;

    Object modifiers;

    Object properties;

    List<Item> items;

    Long operationType;

    Long cashTotalSum;

    Long prepaidSum;

    Long requestNumber;

    List<Item> stornoItems;

    Long fiscalSign;

    Long totalSum;

    Long nds0;
    Long ndsNo;
    Long nds10;
    Long ndsCalculated10;
    Long ndsCalculated18;

    Long receiptCode;

    String buyerAddress;

    String addressToCheckFiscalSign;

    Long postpaymentSum;

    Long prepaymentSum;

    Long provisionSum;

    Long counterSubmissionSum;

    Long creditSum;

    Object userProperty;

    String fnsSite;

    String authorityUri;

    String retailPlace;

    String retailAddress;

    String sellerAddress;

    String fnsUrl;

    public String getKktRegId() {
        return kktRegId;
    }

    public void setKktRegId(String kktRegId) {
        this.kktRegId = kktRegId;
    }

    public Long getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(Long protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public Long getMessageFiscalSign() {
        return messageFiscalSign;
    }

    public void setMessageFiscalSign(Long messageFiscalSign) {
        this.messageFiscalSign = messageFiscalSign;
    }

    public Long getInternetSign() {
        return internetSign;
    }

    public void setInternetSign(Long internetSign) {
        this.internetSign = internetSign;
    }

    public Long getFiscalDocumentFormatVer() {
        return fiscalDocumentFormatVer;
    }

    public void setFiscalDocumentFormatVer(Long fiscalDocumentFormatVer) {
        this.fiscalDocumentFormatVer = fiscalDocumentFormatVer;
    }

    public Long getNds18() {
        return nds18;
    }

    public void setNds18(Long nds18) {
        this.nds18 = nds18;
    }

    public Long getTaxationType() {
        return taxationType;
    }

    public void setTaxationType(Long taxationType) {
        this.taxationType = taxationType;
    }

    public String getRetailPlaceAddress() {
        return retailPlaceAddress;
    }

    public void setRetailPlaceAddress(String retailPlaceAddress) {
        this.retailPlaceAddress = retailPlaceAddress;
    }

    public Long getFiscalDocumentNumber() {
        return fiscalDocumentNumber;
    }

    public void setFiscalDocumentNumber(Long fiscalDocumentNumber) {
        this.fiscalDocumentNumber = fiscalDocumentNumber;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public Long getEcashTotalSum() {
        return ecashTotalSum;
    }

    public void setEcashTotalSum(Long ecashTotalSum) {
        this.ecashTotalSum = ecashTotalSum;
    }

    public String getFiscalDriveNumber() {
        return fiscalDriveNumber;
    }

    public void setFiscalDriveNumber(String fiscalDriveNumber) {
        this.fiscalDriveNumber = fiscalDriveNumber;
    }

    public Long getShiftNumber() {
        return shiftNumber;
    }

    public void setShiftNumber(Long shiftNumber) {
        this.shiftNumber = shiftNumber;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
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

    public Object getModifiers() {
        return modifiers;
    }

    public void setModifiers(Object modifiers) {
        this.modifiers = modifiers;
    }

    public Object getProperties() {
        return properties;
    }

    public void setProperties(Object properties) {
        this.properties = properties;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getOperationType() {
        return operationType;
    }

    public void setOperationType(Long operationType) {
        this.operationType = operationType;
    }

    public Long getCashTotalSum() {
        return cashTotalSum;
    }

    public void setCashTotalSum(Long cashTotalSum) {
        this.cashTotalSum = cashTotalSum;
    }

    public Long getPrepaidSum() {
        return prepaidSum;
    }

    public void setPrepaidSum(Long prepaidSum) {
        this.prepaidSum = prepaidSum;
    }

    public Long getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(Long requestNumber) {
        this.requestNumber = requestNumber;
    }

    public List<Item> getStornoItems() {
        return stornoItems;
    }

    public void setStornoItems(List<Item> stornoItems) {
        this.stornoItems = stornoItems;
    }

    public Long getFiscalSign() {
        return fiscalSign;
    }

    public void setFiscalSign(Long fiscalSign) {
        this.fiscalSign = fiscalSign;
    }

    public Long getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Long totalSum) {
        this.totalSum = totalSum;
    }

    public Long getNds0() {
        return nds0;
    }

    public void setNds0(Long nds0) {
        this.nds0 = nds0;
    }

    public Long getNdsNo() {
        return ndsNo;
    }

    public void setNdsNo(Long ndsNo) {
        this.ndsNo = ndsNo;
    }

    public Long getNds10() {
        return nds10;
    }

    public void setNds10(Long nds10) {
        this.nds10 = nds10;
    }

    public Long getNdsCalculated10() {
        return ndsCalculated10;
    }

    public void setNdsCalculated10(Long ndsCalculated10) {
        this.ndsCalculated10 = ndsCalculated10;
    }

    public Long getNdsCalculated18() {
        return ndsCalculated18;
    }

    public void setNdsCalculated18(Long ndsCalculated18) {
        this.ndsCalculated18 = ndsCalculated18;
    }

    public Long getReceiptCode() {
        return receiptCode;
    }

    public void setReceiptCode(Long receiptCode) {
        this.receiptCode = receiptCode;
    }

    public String getBuyerAddress() {
        return buyerAddress;
    }

    public void setBuyerAddress(String buyerAddress) {
        this.buyerAddress = buyerAddress;
    }

    public String getAddressToCheckFiscalSign() {
        return addressToCheckFiscalSign;
    }

    public void setAddressToCheckFiscalSign(String addressToCheckFiscalSign) {
        this.addressToCheckFiscalSign = addressToCheckFiscalSign;
    }

    public Long getPostpaymentSum() {
        return postpaymentSum;
    }

    public void setPostpaymentSum(Long postpaymentSum) {
        this.postpaymentSum = postpaymentSum;
    }

    public Long getPrepaymentSum() {
        return prepaymentSum;
    }

    public void setPrepaymentSum(Long prepaymentSum) {
        this.prepaymentSum = prepaymentSum;
    }

    public Long getProvisionSum() {
        return provisionSum;
    }

    public void setProvisionSum(Long provisionSum) {
        this.provisionSum = provisionSum;
    }

    public Long getCounterSubmissionSum() {
        return counterSubmissionSum;
    }

    public void setCounterSubmissionSum(Long counterSubmissionSum) {
        this.counterSubmissionSum = counterSubmissionSum;
    }

    public Long getCreditSum() {
        return creditSum;
    }

    public void setCreditSum(Long creditSum) {
        this.creditSum = creditSum;
    }

    public Object getUserProperty() {
        return userProperty;
    }

    public void setUserProperty(Object userProperty) {
        this.userProperty = userProperty;
    }

    public String getFnsSite() {
        return fnsSite;
    }

    public void setFnsSite(String fnsSite) {
        this.fnsSite = fnsSite;
    }

    public String getAuthorityUri() {
        return authorityUri;
    }

    public void setAuthorityUri(String authorityUri) {
        this.authorityUri = authorityUri;
    }

    public String getRetailPlace() {
        return retailPlace;
    }

    public void setRetailPlace(String retailPlace) {
        this.retailPlace = retailPlace;
    }

    public String getRetailAddress() {
        return retailAddress;
    }

    public void setRetailAddress(String retailAddress) {
        this.retailAddress = retailAddress;
    }

    public String getSellerAddress() {
        return sellerAddress;
    }

    public void setSellerAddress(String sellerAddress) {
        this.sellerAddress = sellerAddress;
    }

    public String getFnsUrl() {
        return fnsUrl;
    }

    public void setFnsUrl(String fnsUrl) {
        this.fnsUrl = fnsUrl;
    }
}
