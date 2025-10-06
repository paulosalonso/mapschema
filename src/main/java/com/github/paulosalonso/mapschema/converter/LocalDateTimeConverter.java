package com.github.paulosalonso.mapschema.converter;

import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LocalDateTimeConverter {

    public static LocalDateTime fromString(String value) {
        return LocalDateTime.from(ISO_LOCAL_DATE_TIME.parse(value));
    }

    public static String toString(LocalDateTime value) {
        return ISO_LOCAL_DATE_TIME.format(value);
    }
}
