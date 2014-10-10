package com.piotrglazar.nightowl.util;

import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collector;

@SuppressWarnings("unchecked")
public final class MoreCollectors {

    private MoreCollectors() {
        // Utility class
    }

    public static <T> Collector<T, ?, List<T>> toImmutableList() {
        return Collector.of((Supplier<List<T>>) LinkedList::new, List::add, (left, right) -> { left.addAll(right); return left; },
                (list) -> (List<T>) new ImmutableList.Builder<>().addAll(list).build());
    }
}
