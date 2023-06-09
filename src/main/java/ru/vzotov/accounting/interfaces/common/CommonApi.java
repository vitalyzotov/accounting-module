package ru.vzotov.accounting.interfaces.common;

import java.io.Serializable;

public interface CommonApi {
    record Money(long amount, String currency) implements Serializable {
    }
}
