package ru.vzotov.cashreceipt.application.billchecker;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
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

    public Receipt parse(InputStream in) throws IOException {
        ObjectMapper mapper = createMapper();
        ReceiptDTO receipt = mapper.readValue(in, ReceiptDTO.class);

        return parse(receipt);
    }

    @Override
    public Receipt parse(String data) throws IOException {
        ObjectMapper mapper = createMapper();
        ReceiptDTO receipt = mapper.readValue(data, ReceiptDTO.class);

        return parse(receipt);
    }

    private ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule());

        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        return mapper;
    }

    private Receipt parse(ReceiptDTO receipt) {
        final ZoneId utcZone = ZoneId.of("GMT");//TODO: почему GMT?

        Function<List<ItemDTO>, List<ru.vzotov.cashreceipt.domain.model.Item>> itemsMapper =
                (items) -> IntStream.range(0, items.size()).mapToObj((index) -> {
                    ItemDTO i = items.get(index);
                    return new ru.vzotov.cashreceipt.domain.model.Item(i.name, Money.kopecks(i.price), i.quantity, Money.kopecks(i.sum), index);
                }).collect(Collectors.toList());

        return new Receipt(
                receipt.dateTime.atZone(utcZone).toLocalDateTime(),
                ReceiptOperationType.of(receipt.operationType),
                receipt.requestNumber,
                new FiscalInfo(
                        null,
                        receipt.kktRegId,
                        new FiscalSign(receipt.fiscalSign),
                        receipt.fiscalDocumentNumber.toString(),
                        receipt.fiscalDriveNumber
                ),
                Marketing.emptyMarketing(),
                new ShiftInfo(receipt.shiftNumber, receipt.operator),
                new RetailPlace(
                        null,
                        new Inn(receipt.userInn),
                        receipt.retailPlaceAddress == null ? null : new Address(receipt.retailPlaceAddress),
                        receipt.taxationType
                ),
                new Products(
                        Optional.ofNullable(receipt.items).map(itemsMapper).orElse(Collections.emptyList()),
                        Optional.ofNullable(receipt.stornoItems).map(itemsMapper).orElse(Collections.emptyList()),
                        Money.kopecks(receipt.totalSum)
                ),
                new PaymentInfo(Money.kopecks(receipt.cashTotalSum), Money.kopecks(receipt.ecashTotalSum))
        );
    }

}
