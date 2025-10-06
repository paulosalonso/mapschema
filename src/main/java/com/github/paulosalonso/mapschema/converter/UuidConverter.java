package com.github.paulosalonso.mapschema.converter;

import lombok.NoArgsConstructor;

import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UuidConverter {

    public static UUID fromString(String value) {
        return UUID.fromString(value);
    }

    public static String toString(UUID value) {
        return value.toString();
    }
}
