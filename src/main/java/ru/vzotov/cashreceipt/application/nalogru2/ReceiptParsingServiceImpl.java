package ru.vzotov.cashreceipt.application.nalogru2;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.vzotov.cashreceipt.application.ReceiptParsingService;
import ru.vzotov.cashreceipt.domain.model.Address;
import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.cashreceipt.domain.model.ReceiptOperationType;
import ru.vzotov.cashreceipt.domain.model.FiscalInfo;
import ru.vzotov.cashreceipt.domain.model.Marketing;
import ru.vzotov.cashreceipt.domain.model.PaymentInfo;
import ru.vzotov.cashreceipt.domain.model.Products;
import ru.vzotov.cashreceipt.domain.model.RetailPlace;
import ru.vzotov.cashreceipt.domain.model.ShiftInfo;
import ru.vzotov.accounting.infrastructure.json.OffsetDateTimeDeserializer;
import ru.vzotov.domain.model.Money;
import ru.vzotov.fiscal.FiscalSign;
import ru.vzotov.fiscal.Inn;

import java.io.IOException;
import java.io.InputStream;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

public class ReceiptParsingServiceImpl implements ReceiptParsingService {

    private static final String MOSCOW_ZONE_ID = "Europe/Moscow";
    private static final ZoneId MOSCOW_ZONE = ZoneId.of(MOSCOW_ZONE_ID);

    @Override
    public Receipt parse(InputStream in) throws IOException {
        ObjectMapper mapper = createMapper();
        TicketInfo root = mapper.readValue(in, TicketInfo.class);

        ru.vzotov.cashreceipt.application.nalogru2.Receipt receipt =
                root == null ? null :
                        root.ticket == null ? null :
                                root.ticket.document == null ? null :
                                        root.ticket.document.receipt;

        return parse(receipt);
    }

    @Override
    public Receipt parse(String data) throws IOException {
        ObjectMapper mapper = createMapper();
        TicketInfo root = mapper.readValue(data, TicketInfo.class);
        ru.vzotov.cashreceipt.application.nalogru2.Receipt receipt =
                root == null ? null :
                        root.ticket == null ? null :
                                root.ticket.document == null ? null :
                                        root.ticket.document.receipt;

        return parse(receipt);

    }

    private Receipt parse(ru.vzotov.cashreceipt.application.nalogru2.Receipt receipt) {
        if(receipt == null) return null;

        Function<List<Item>, List<ru.vzotov.cashreceipt.domain.model.Item>> itemsMapper =
                (items) -> IntStream.range(0, items.size()).mapToObj((index) -> {
                    Item i = items.get(index);
                    return new ru.vzotov.cashreceipt.domain.model.Item(i.name, Money.kopecks(i.price), i.quantity, Money.kopecks(i.sum), index);
                }).collect(Collectors.toList());

        final Products products = new Products(
                ofNullable(receipt.items).map(itemsMapper).orElse(Collections.emptyList()),
                ofNullable(receipt.stornoItems).map(itemsMapper).orElse(Collections.emptyList()),
                Money.kopecks(receipt.totalSum)
        );
        final PaymentInfo paymentInfo = new PaymentInfo(Money.kopecks(receipt.cashTotalSum), Money.kopecks(receipt.ecashTotalSum));
        final FiscalInfo fiscalInfo = new FiscalInfo(
                null,
                receipt.kktRegId.trim(),
                new FiscalSign(receipt.fiscalSign),
                receipt.fiscalDocumentNumber.toString(),
                receipt.fiscalDriveNumber
        );

        final RetailPlace place = new RetailPlace(
                receipt.user == null ? null : receipt.user.trim(),
                ofNullable(receipt.userInn).map(String::trim).map(Inn::new).orElseThrow(NoSuchElementException::new),
                receipt.retailPlaceAddress == null ? null : new Address(receipt.retailPlaceAddress),
                receipt.taxationType
        );
        return new Receipt(
                receipt.dateTime.atZone(MOSCOW_ZONE).toLocalDateTime(),
                ReceiptOperationType.of(receipt.operationType),
                receipt.requestNumber,
                fiscalInfo,
                Marketing.emptyMarketing(),
                new ShiftInfo(receipt.shiftNumber, receipt.operator),
                place,
                products,
                paymentInfo
        );
    }

    private ObjectMapper createMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(module);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper;
    }

}
