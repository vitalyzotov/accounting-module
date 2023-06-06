package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import java.time.LocalDate;
import java.util.List;

public record CardDTO(String cardNumber, String owner, LocalDate validThru, String issuer,
                      List<AccountBindingDTO> accounts) {
}

