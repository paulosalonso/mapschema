package com.github.paulosalonso.mapschema.converter;

import lombok.RequiredArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
public class EnumConverter<T extends Enum<T>> {

    private final Class<T> enumType;

    public static <T extends Enum<T>> EnumConverter<T> of(Class<T> enumType) {
        return new EnumConverter<>(enumType);
    }

    public T toEnum(String value) {
        return Enum.valueOf(enumType, value.toUpperCase());
    }
}
