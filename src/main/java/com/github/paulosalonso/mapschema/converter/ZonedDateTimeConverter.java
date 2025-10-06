package com.github.paulosalonso.mapschema.converter;

import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ISO_ZONED_DATE_TIME;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ZonedDateTimeConverter {

    public static ZonedDateTime fromString(String value) {
        return ZonedDateTime.from(ISO_ZONED_DATE_TIME.parse(value));
    }

    public static String toString(ZonedDateTime value) {
        return ISO_ZONED_DATE_TIME.format(value);
    }
}
