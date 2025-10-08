package com.github.paulosalonso.mapschema.converter;

import com.github.paulosalonso.mapschema.MapSchema;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.function.Function;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class MapSchemaConverter {

    public static Map<String, Object> toSource(MapSchema mapSchema) {
        return mapSchema.getSource();
    }

    public static <T extends MapSchema> Function<Map<String, Object>, T> toMapSchema(Function<Map<String, Object>, T> converter) {
        return CachedConverter.withConverter(converter)::convert;
    }
}
