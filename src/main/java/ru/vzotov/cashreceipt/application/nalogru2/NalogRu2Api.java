package ru.vzotov.cashreceipt.application.nalogru2;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

public interface NalogRu2Api {
    record AuthRequest(String client_secret, String inn, String password) {
    }

    record AuthResponse(String sessionId, String refresh_token) {
    }

    record Document(Receipt receipt) {
    }

    record Item(Long sum, Double quantity, Long price, Object modifiers, Object properties, String name, Long nds,
                Long nds0, Long nds10, Long nds18, Long ndsCalculated10, Long ndsCalculated18, Long ndsNo, Long ndsRate,
                Long ndsSum, boolean storno, Long calculationSubjectSign, Long calculationTypeSign, Long paymentType,
                Long productType, String unit) {
    }

    record Organization(String name, String inn, String logo) {
    }

    record Process(OffsetDateTime time, Long result) {
    }

    /**
     * @param dateTime          1596782760
     * @param rawData           base64 encoded string
     * @param fiscalDriveNumber "8710000100313204"
     */
    record Receipt(String kktRegId, Long protocolVersion, Long messageFiscalSign, Long internetSign,
                   Long fiscalDocumentFormatVer, Long nds18, Long taxationType, String retailPlaceAddress,
                   Long fiscalDocumentNumber, String operator, Instant dateTime, Object message, String rawData,
                   Long ecashTotalSum, String fiscalDriveNumber, Long shiftNumber, String senderAddress, String user,
                   String userInn, Object modifiers, Object properties, List<Item> items, Long operationType,
                   Long cashTotalSum, Long prepaidSum, Long requestNumber, List<Item> stornoItems, Long fiscalSign,
                   Long totalSum, Long nds0, Long ndsNo, Long nds10, Long ndsCalculated10, Long ndsCalculated18,
                   Long receiptCode, String buyerAddress, String addressToCheckFiscalSign, Long postpaymentSum,
                   Long prepaymentSum, Long provisionSum, Long counterSubmissionSum, Long creditSum, Object userProperty,
                   String fnsSite, String authorityUri, String retailPlace, String retailAddress, String sellerAddress,
                   String fnsUrl) {
    }

    record StatusDescription(@JsonProperty("long") String longDescription,
                                    @JsonProperty("short") String shortDescription) {
    }

    record Ticket(String kind, String id, Long status) {
    }

    record TicketData(Document document) {
    }

    record TicketInfo(String id, Long status, String kind, OffsetDateTime createdAt,
                      StatusDescription statusDescription, String qr, TicketOperation operation,
                      List<Process> process, TicketQuery query, TicketData ticket, Organization organization) {
    }

    record TicketOperation(OffsetDateTime date, Long type, Long sum) {
    }

    record TicketQuery(Long operationType, Long sum, Long documentId, String fsId, String fiscalSign,
                       LocalDateTime date) {
    }

    record TicketRequest(String qr) {
    }
}
