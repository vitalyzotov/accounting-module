package ru.vzotov.cashreceipt.application.nalogru;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
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
import ru.vzotov.domain.model.Money;
import ru.vzotov.fiscal.FiscalSign;
import ru.vzotov.fiscal.Inn;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ReceiptParsingServiceImpl implements ReceiptParsingService {

    @Override
    public Receipt parse(PersonId owner, InputStream in) throws IOException {
        ObjectMapper mapper = createMapper();
        NalogRuApi.NalogRuRoot root = mapper.readValue(in, NalogRuApi.NalogRuRoot.class);
        NalogRuApi.Receipt receipt = root.document().receipt();

        return parse(owner, receipt);
    }

    @Override
    public Receipt parse(PersonId owner, String data) throws IOException {
        ObjectMapper mapper = createMapper();
        NalogRuApi.NalogRuRoot root = mapper.readValue(data, NalogRuApi.NalogRuRoot.class);
        NalogRuApi.Receipt receipt = root.document().receipt();

        return parse(owner, receipt);
    }

    private Receipt parse(PersonId owner, NalogRuApi.Receipt receipt) {
        final ZoneId moscowZone = ZoneId.systemDefault();

        Function<List<NalogRuApi.Item>, List<ru.vzotov.cashreceipt.domain.model.Item>> itemsMapper =
                (items) -> IntStream.range(0, items.size()).mapToObj((index) -> {
                    NalogRuApi.Item i = items.get(index);
                    return new ru.vzotov.cashreceipt.domain.model.Item(i.name(), Money.kopecks(i.price()), i.quantity(), Money.kopecks(i.sum()), index);
                }).collect(Collectors.toList());

        final Products products = new Products(
                Optional.ofNullable(receipt.items()).map(itemsMapper).orElse(Collections.emptyList()),
                Optional.ofNullable(receipt.stornoItems()).map(itemsMapper).orElse(Collections.emptyList()),
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
                new Inn(receipt.userInn()),
                receipt.retailPlaceAddress() == null ? null : new Address(receipt.retailPlaceAddress()),
                receipt.taxationType()
        );
        return new Receipt(
                owner,
                receipt.dateTime().atZone(moscowZone).toLocalDateTime(),
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

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        return mapper;
    }

}
