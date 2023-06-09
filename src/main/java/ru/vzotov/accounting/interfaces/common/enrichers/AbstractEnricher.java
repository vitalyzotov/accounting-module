package ru.vzotov.accounting.interfaces.common.enrichers;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class AbstractEnricher<T> implements Enricher<T> {

    @Override
    public <U> U apply(Collection<T> data, Collector<T, ?, U> collector) {
        return data.stream().map(this::apply).collect(collector);
    }

    public List<T> list(Collection<T> data) {
        return apply(data, Collectors.toList());
    }
}
