package ru.vzotov.accounting.interfaces.accounting.facade.impl.assemblers;

public class ColorAssembler {

    public static String toDTO(Integer color) {
        return color == null ? null :
                String.format("#%08X", color);
    }
}
