package ru.vzotov.accounting.interfaces.accounting.facade.dto;

import ru.vzotov.cashreceipt.domain.model.CheckState;

public enum DealDTOExpansion {
    RECEIPTS("receipts"),
    OPERATIONS("operations"),
    CARD_OPERATIONS("card_operations"),
    PURCHASES("purchases");

    private final String value;

    DealDTOExpansion(String value) {
        this.value = value;
    }

    public static DealDTOExpansion of(String value) {
        for (DealDTOExpansion opt : DealDTOExpansion.values()) {
            if (opt.value.equalsIgnoreCase(value)) return opt;
        }
        throw new IllegalArgumentException("Unknown expansion");
    }

}
