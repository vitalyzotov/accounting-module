package ru.vzotov.cashreceipt.application.impl;

import ru.vzotov.cashreceipt.domain.model.QRCode;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class RandomSelectionStrategy implements ReceiptSelectionStrategy {

    @Override
    public Set<QRCode> select(Collection<QRCode> codes, int count) {
        return getRandomElements(codes, count);
    }

    /**
     * Выбираем из списка элементы в случайном порядке, используя нормальное распределение,
     * чтобы повысить вероятность выбора чеков с минимальным счетчиком загрузок
     *
     * @param from  список всех элементов
     * @param count сколько элементов нужно выбрать
     * @param <T>   тип
     * @return набор выбранных элементов
     */
    private <T extends QRCode> Set<T> getRandomElements(final Collection<T> from, final int count) {
        final int size = from.size();
        final Set<T> result = (size > count) ? new HashSet<>(count) : new HashSet<>(from);
        if (size > count) {
            final List<T> source = from.stream()
                    .sorted(Comparator.comparing(QRCode::loadingTryCount))
                    .collect(Collectors.toList());
            Random random = new Random();
            for (int i = 0; i < count; i++) {
                double v = random.nextGaussian();
                int index = Math.max(0, Math.min((int) (Math.abs(v) * source.size()), source.size()));
                result.add(source.remove(index));
            }
        }

        return result;
    }
}
