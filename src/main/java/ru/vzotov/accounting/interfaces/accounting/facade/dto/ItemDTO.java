package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.accounting.interfaces.common.dto.MoneyDTO;

import java.io.Serializable;

public record ItemDTO(String name, MoneyDTO price, double quantity, MoneyDTO sum, Integer index,
                      String category) implements Serializable {
}
