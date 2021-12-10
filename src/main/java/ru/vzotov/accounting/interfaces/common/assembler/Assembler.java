package ru.vzotov.accounting.interfaces.common.assembler;

import java.util.List;

public interface Assembler<D,M> {
    D toDTO(M model);
    List<D> toDTOList(List<M> list);
}
