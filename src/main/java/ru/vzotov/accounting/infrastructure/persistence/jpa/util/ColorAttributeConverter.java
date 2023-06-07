package ru.vzotov.accounting.infrastructure.persistence.jpa.util;

import jakarta.persistence.AttributeConverter;

public class ColorAttributeConverter implements AttributeConverter<Integer, String> {
    @Override
    public String convertToDatabaseColumn(Integer color) {
        return color == null ? null :
                String.format("#%08X", color);
    }

    @Override
    public Integer convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        } else {
            return s.charAt(0) == '#' ?
                    Integer.parseUnsignedInt(s.substring(1), 16) :
                    Integer.parseUnsignedInt(s, 16);
        }
    }
}
