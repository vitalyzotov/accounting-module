package ru.vzotov.cashreceipt.application.billchecker;

import java.time.Instant;
import java.util.List;

@SuppressWarnings("unused")
class Receipt {

    String kktNumber;

    String kktRegId;

    Long taxationType;

    String retailPlaceAddress;

    Long fiscalDocumentNumber;

    String operator;

    Instant dateTime; //1529157300

    Long ecashTotalSum;

    String fiscalDriveNumber; // "8710000100313204"

    Long shiftNumber;

    String senderAddress;

    String user;
    String userInn;

    List<Item> items;

    Long operationType;

    Long cashTotalSum;

    Long requestNumber;

    List<Item> stornoItems;

    Long fiscalSign;

    Long totalSum;

    Long markup;

    Long markupSum;

    Long discount;

    Long discountSum;

    Long nds0;

    Long nds10;

    Long nds18;

    Long ndsCalculated10;

    Long ndsCalculated18;

    Long ndsNo;

    Object modifiers;
}
