package ru.vzotov.accounting.interfaces.accounting.rest.dto;

import java.util.List;

public record AccountCreateRequest(String number, String name, String bankId, String currency, String owner,
                                   List<String> aliases) {

}
