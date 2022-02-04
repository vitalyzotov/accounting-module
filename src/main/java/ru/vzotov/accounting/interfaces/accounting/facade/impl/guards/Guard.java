package ru.vzotov.accounting.interfaces.accounting.facade.impl.guards;

import java.util.List;

public interface Guard<T> {
    <E extends T> E accessing(E entity);

    <E extends T> List<E> filter(List<E> list);
}
