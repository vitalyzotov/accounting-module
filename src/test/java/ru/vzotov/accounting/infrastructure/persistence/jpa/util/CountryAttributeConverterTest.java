package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.vzotov.banking.domain.model.Country;

@RunWith(JUnit4.class)
public class CountryAttributeConverterTest {

    @Test
    public void convertToDatabaseColumn() {
        final CountryAttributeConverter converter = new CountryAttributeConverter();
        final String dbValue = converter.convertToDatabaseColumn(new Country("643"));
        Assert.assertEquals("RUS", dbValue);
    }

    @Test
    public void convertToEntityAttribute() {
        final CountryAttributeConverter converter = new CountryAttributeConverter();
        final Country country = new Country("643");
        Assert.assertEquals(country, converter.convertToEntityAttribute("RUS"));
        Assert.assertEquals(country, converter.convertToEntityAttribute("643"));
        Assert.assertEquals(country, converter.convertToEntityAttribute("RU"));
    }
}
