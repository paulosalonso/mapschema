package com.github.paulosalonso.mapschema.converter;

import lombok.RequiredArgsConstructor;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class CachedConverter<INPUT, OUTPUT> {

    private final Map<INPUT, OUTPUT> CACHE = new IdentityHashMap<>();

    private final Function<INPUT, OUTPUT> factory;

    public static <INPUT, OUTPUT> CachedConverter<INPUT, OUTPUT> withFactory(Function<INPUT, OUTPUT> factory) {
        return new CachedConverter<>(factory);
    }

    public OUTPUT convert(INPUT source) {
        return CACHE.computeIfAbsent(source, factory);
    }

}
