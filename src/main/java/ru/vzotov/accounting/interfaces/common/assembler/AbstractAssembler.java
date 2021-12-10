package ru.vzotov.accounting.interfaces.common.assembler;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractAssembler<D, M> implements Assembler<D, M> {

    @Override
    public List<D> toDTOList(List<M> list) {
        return list == null ? null : list.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
