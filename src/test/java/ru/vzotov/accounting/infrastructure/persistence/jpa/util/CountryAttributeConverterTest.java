package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import org.junit.jupiter.api.Test;
import ru.vzotov.banking.domain.model.Country;

import static org.assertj.core.api.Assertions.assertThat;

public class CountryAttributeConverterTest {

    @Test
    public void convertToDatabaseColumn() {
        final CountryAttributeConverter converter = new CountryAttributeConverter();
        final String dbValue = converter.convertToDatabaseColumn(new Country("643"));
        assertThat(dbValue).isEqualTo("RUS");
    }

    @Test
    public void convertToEntityAttribute() {
        final CountryAttributeConverter converter = new CountryAttributeConverter();
        final Country country = new Country("643");
        assertThat(converter.convertToEntityAttribute("RUS")).isEqualTo(country);
        assertThat(converter.convertToEntityAttribute("643")).isEqualTo(country);
        assertThat(converter.convertToEntityAttribute("RU")).isEqualTo(country);
    }
}
