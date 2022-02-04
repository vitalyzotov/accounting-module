package ru.vzotov.cashreceipt.application;

import ru.vzotov.cashreceipt.domain.model.Receipt;
import ru.vzotov.person.domain.model.PersonId;

import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("unused")
public interface ReceiptParsingService {
    Receipt parse(PersonId owner, InputStream in) throws IOException;

    Receipt parse(PersonId owner, String data) throws IOException;
}
