package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.io.Serializable;
import java.util.List;

public record AccountDTO(String number, String name, String bankId, String currency, String owner,
                         List<String> aliases) implements Serializable {

}
