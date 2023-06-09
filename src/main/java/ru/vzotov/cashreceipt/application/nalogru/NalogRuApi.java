package ru.vzotov.cashreceipt.application.nalogru;

import java.time.LocalDateTime;
import java.util.List;

public interface NalogRuApi {
    record Document(Receipt receipt) {
    }

    record Item(Long sum, Double quantity, Long price, Object modifiers, Object properties, String name, Long nds,
                    Long nds0, Long nds10, Long nds18, Long ndsCalculated10, Long ndsCalculated18, Long ndsNo, Long ndsRate,
                    Long ndsSum, boolean storno, Long calculationSubjectSign, Long calculationTypeSign, Long paymentType,
                    Long productType, String unit) {
    }

    record NalogRuRoot(Document document) {
    }

    /**
     * @param dateTime          "2018-06-16T13:55:00"
     * @param rawData           base64 encoded string
     * @param fiscalDriveNumber "8710000100313204"
     */
    record Receipt(String kktRegId, Long protocolVersion, Long messageFiscalSign, Long internetSign,
                   Long fiscalDocumentFormatVer, Long nds18, Long taxationType, String retailPlaceAddress,
                   Long fiscalDocumentNumber, String operator, LocalDateTime dateTime, Object message, String rawData,
                   Long ecashTotalSum, String fiscalDriveNumber, Long shiftNumber, String senderAddress, String user,
                   String userInn, Object modifiers, Object properties, List<Item> items, Long operationType,
                   Long cashTotalSum, Long prepaidSum, Long requestNumber, List<Item> stornoItems, Long fiscalSign,
                   Long totalSum, Long nds0, Long ndsNo, Long nds10, Long ndsCalculated10, Long ndsCalculated18,
                   Long receiptCode, String buyerAddress, String addressToCheckFiscalSign, Long postpaymentSum,
                   Long prepaymentSum, Long provisionSum, Long counterSubmissionSum, Long creditSum, Object userProperty,
                   String fnsSite, String authorityUri, String retailPlace, String retailAddress, String sellerAddress,
                   String fnsUrl) {

    }
}
