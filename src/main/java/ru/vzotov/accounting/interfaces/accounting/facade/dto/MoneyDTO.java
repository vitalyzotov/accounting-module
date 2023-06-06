package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.io.Serializable;

public record MoneyDTO(long amount, String currency) implements Serializable {
}
