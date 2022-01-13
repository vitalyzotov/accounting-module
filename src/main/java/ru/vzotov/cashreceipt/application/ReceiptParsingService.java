package ru.vzotov.cashreceipt.application;

import ru.vzotov.cashreceipt.domain.model.Receipt;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public interface ReceiptParsingService {
    Receipt parse(InputStream in) throws IOException;

    Receipt parse(String data) throws IOException;
}
