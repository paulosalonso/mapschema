package com.github.paulosalonso.mapschema.converter;

import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class BigDecimalConverter {

    public static BigDecimal fromNumber(Number value) {
        return BigDecimal.valueOf(value.doubleValue());
    }

    public static BigDecimal fromString(String value) {
        return new BigDecimal(value);
    }

    public static Integer toInteger(BigDecimal value) {
        return value.intValue();
    }

    public static Short toShort(BigDecimal value) {
        return value.shortValue();
    }

    public static Long toLong(BigDecimal value) {
        return value.longValue();
    }

    public static Float toFloat(BigDecimal value) {
        return value.floatValue();
    }

    public static Double toDouble(BigDecimal value) {
        return value.doubleValue();
    }

    public static String toString(BigDecimal value) {
        return value.toString();
    }
}
