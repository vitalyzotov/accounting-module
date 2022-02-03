package ru.vzotov.accounting.interfaces.accounting.facade.impl;

import java.util.List;

public interface Guard<T> {
    T accessing(T entity);

    List<T> filter(List<T> list);
}
