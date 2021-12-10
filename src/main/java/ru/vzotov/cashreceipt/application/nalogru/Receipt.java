package ru.vzotov.cashreceipt.application.nalogru;

import java.time.LocalDateTime;
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

    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    LocalDateTime dateTime; //"2018-06-16T13:55:00"

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
}
