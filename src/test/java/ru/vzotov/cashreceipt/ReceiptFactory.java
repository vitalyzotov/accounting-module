package ru.vzotov.cashreceipt;

import ru.vzotov.cashreceipt.domain.model.Address;
import ru.vzotov.cashreceipt.domain.model.Check;
import ru.vzotov.cashreceipt.domain.model.CheckOperationType;
import ru.vzotov.cashreceipt.domain.model.FiscalInfo;
import ru.vzotov.cashreceipt.domain.model.Marketing;
import ru.vzotov.cashreceipt.domain.model.PaymentInfo;
import ru.vzotov.cashreceipt.domain.model.Products;
import ru.vzotov.cashreceipt.domain.model.RetailPlace;
import ru.vzotov.cashreceipt.domain.model.ShiftInfo;
import ru.vzotov.cashreceipt.application.billchecker.ReceiptParsingServiceImpl;
import ru.vzotov.domain.model.Money;
import ru.vzotov.fiscal.FiscalSign;
import ru.vzotov.fiscal.Inn;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Collections;

public class ReceiptFactory {

    public Check createCheckSimple() {
        return new Check(
                LocalDateTime.now(),
                CheckOperationType.INCOME,
                1L,
                new FiscalInfo(
                        "0001107425024311",
                        new FiscalSign("2334756689"),
                        "21068",
                        "8710000100312991"
                ),
                Marketing.emptyMarketing(),
                new ShiftInfo(1L, "John"),
                new RetailPlace(null, new Inn("2310031475"), new Address("Саратов, ул. Моторная, д. 6"), 1L),
                new Products(Collections.emptyList(), Collections.emptyList(), Money.rubles(10)),
                new PaymentInfo(Money.kopecks(0), Money.kopecks(1000))
        );
    }

    public Check createCheckWithLongUser() {
        return new Check(
                LocalDateTime.now(),
                CheckOperationType.INCOME,
                1L,
                new FiscalInfo(
                        "0001107425024311",
                        new FiscalSign("2334756689"),
                        "21068",
                        "8710000100312991"
                ),
                Marketing.emptyMarketing(),
                new ShiftInfo(1L, "John"),
                new RetailPlace("ООО\"ЮНАЙТЭД ТРЭЙДИНГ\" 121357,г.Москва ул.Верейская,д.29,стр.33,этаж 5,ком 7-11;16",
                        new Inn("2310031475"), new Address("Саратов, ул. Моторная, д. 6"), 1L),
                new Products(Collections.emptyList(), Collections.emptyList(), Money.rubles(10)),
                new PaymentInfo(Money.kopecks(0), Money.kopecks(1000))
        );
    }

    public Check createCheckFromBillchecker(String path) throws IOException {
        ReceiptParsingServiceImpl parser =
                new ReceiptParsingServiceImpl();

        try (InputStream data = getClass().getResourceAsStream(path)) {
            return parser.parse(data);
        }
    }

    public Check createReceiptFromJson(String path) throws IOException {
        ru.vzotov.cashreceipt.application.nalogru.ReceiptParsingServiceImpl parser =
                new ru.vzotov.cashreceipt.application.nalogru.ReceiptParsingServiceImpl();

        try (InputStream data = getClass().getResourceAsStream(path)) {
            return parser.parse(data);
        }
    }

    public Check createReceiptFromJson2(String path) throws IOException {
        ru.vzotov.cashreceipt.application.nalogru2.ReceiptParsingServiceImpl parser =
                new ru.vzotov.cashreceipt.application.nalogru2.ReceiptParsingServiceImpl();

        try (InputStream data = getClass().getResourceAsStream(path)) {
            return parser.parse(data);
        }
    }
}
