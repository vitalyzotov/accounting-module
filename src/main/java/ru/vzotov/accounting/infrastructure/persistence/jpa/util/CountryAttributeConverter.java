package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import ru.vzotov.banking.domain.model.Country;

import jakarta.persistence.AttributeConverter;

public class CountryAttributeConverter implements AttributeConverter<Country, String> {
    @Override
    public String convertToDatabaseColumn(Country country) {
        return country == null ? null : country.code();
    }

    @Override
    public Country convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Country(dbData);
    }
}
