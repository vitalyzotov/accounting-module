package ru.vzotov.accounting.interfaces.common.enrichers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;

public interface Enricher<T> {
    T apply(T ref);

    <U> U apply(Collection<T> data, Collector<T, ?, U> collector);

    List<T> list(Collection<T> data);
}
