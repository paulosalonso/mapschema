package com.github.paulosalonso.mapschema.converter;

import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LocalDateConverter {

    public static LocalDate fromString(String value) {
        return LocalDate.from(ISO_LOCAL_DATE.parse(value));
    }

    public static String toString(LocalDate value) {
        return ISO_LOCAL_DATE.format(value);
    }
}
