package ru.vzotov.cashreceipt.application.nalogru2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.vzotov.accounting.infrastructure.json.OffsetDateTimeDeserializer;
import ru.vzotov.cashreceipt.application.ReceiptParsingService;
import ru.vzotov.cashreceipt.domain.model.Address;
import ru.vzotov.cashreceipt.domain.model.FiscalInfo;
import ru.vzotov.cashreceipt.domain.model.Item;
import ru.vzotov.cashreceipt.domain.model.Marketing;
import ru.vzotov.cashreceipt.domain.model.PaymentInfo;
import ru.vzotov.cashreceipt.domain.model.Products;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptOperationType;
import ru.vzotov.cashreceipt.domain.model.RetailPlace;
import ru.vzotov.cashreceipt.domain.model.ShiftInfo;
import ru.vzotov.domain.model.Money;
import ru.vzotov.fiscal.FiscalSign;
import ru.vzotov.fiscal.Inn;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

public class ReceiptParsingServiceImpl implements ReceiptParsingService {

    private static final String MOSCOW_ZONE_ID = "Europe/Moscow";
    private static final ZoneId MOSCOW_ZONE = ZoneId.of(MOSCOW_ZONE_ID);
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule().addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer()))
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    @Override
    public Receipt parse(PersonId owner, InputStream in) throws IOException {
        return parse(owner, mapReceipt(mapper.readValue(in, NalogRu2Api.TicketInfo.class)));
    }

    @Override
    public Receipt parse(PersonId owner, String data) throws IOException {
        return parse(owner, mapReceipt(mapper.readValue(data, NalogRu2Api.TicketInfo.class)));

    }

    private NalogRu2Api.Receipt mapReceipt(NalogRu2Api.TicketInfo root) {
        return Optional.ofNullable(root)
                .map(NalogRu2Api.TicketInfo::ticket)
                .map(NalogRu2Api.TicketData::document)
                .map(NalogRu2Api.Document::receipt)
                .orElse(null);
    }

    private List<Item> mapItems(List<NalogRu2Api.Item> items) {
        return IntStream.range(0, items.size())
                .mapToObj((index) -> {
                    final NalogRu2Api.Item i = items.get(index);
                    return new Item(
                            i.name(), Money.kopecks(i.price()), i.quantity(), Money.kopecks(i.sum()), index);
                }).collect(Collectors.toList());
    }

    private Receipt parse(PersonId owner, NalogRu2Api.Receipt receipt) {
        if (receipt == null) return null;

        final Products products = new Products(
                ofNullable(receipt.items()).map(this::mapItems).orElse(Collections.emptyList()),
                ofNullable(receipt.stornoItems()).map(this::mapItems).orElse(Collections.emptyList()),
                Money.kopecks(receipt.totalSum())
        );
        final PaymentInfo paymentInfo = new PaymentInfo(Money.kopecks(receipt.cashTotalSum()), Money.kopecks(receipt.ecashTotalSum()));
        final FiscalInfo fiscalInfo = new FiscalInfo(
                null,
                receipt.kktRegId().trim(),
                new FiscalSign(receipt.fiscalSign()),
                receipt.fiscalDocumentNumber().toString(),
                receipt.fiscalDriveNumber()
        );

        final RetailPlace place = new RetailPlace(
                receipt.user() == null ? null : receipt.user().trim(),
                ofNullable(receipt.userInn()).map(String::trim).map(Inn::new).orElseThrow(NoSuchElementException::new),
                receipt.retailPlaceAddress() == null ? null : new Address(receipt.retailPlaceAddress()),
                receipt.taxationType()
        );
        return new Receipt(
                owner,
                receipt.dateTime().atZone(MOSCOW_ZONE).toLocalDateTime(),
                ReceiptOperationType.of(receipt.operationType()),
                receipt.requestNumber(),
                fiscalInfo,
                Marketing.emptyMarketing(),
                new ShiftInfo(receipt.shiftNumber(), receipt.operator()),
                place,
                products,
                paymentInfo
        );
    }

}
