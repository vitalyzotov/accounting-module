package ru.vzotov.cashreceipt.application.impl;

import ru.vzotov.cashreceipt.domain.model.CheckQRCode;

import java.util.Collection;
import java.util.Set;

@FunctionalInterface
public interface ReceiptSelectionStrategy {
    Set<CheckQRCode> select(final Collection<CheckQRCode> codes, final int count);
}
