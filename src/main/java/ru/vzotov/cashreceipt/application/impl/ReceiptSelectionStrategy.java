package ru.vzotov.cashreceipt.application.impl;

import ru.vzotov.cashreceipt.domain.model.QRCode;

import java.util.Collection;
import java.util.Set;

@FunctionalInterface
public interface ReceiptSelectionStrategy {
    Set<QRCode> select(final Collection<QRCode> codes, final int count);
}
