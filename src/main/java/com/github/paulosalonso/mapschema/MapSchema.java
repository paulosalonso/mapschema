package com.github.paulosalonso.mapschema;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface MapSchema {

    MapSchema getParent();

    void setParent(MapSchema parent, String key);

    Map<String, Object> getSource();

    void setSource(Map<String, Object> source);

    Object get(String key);

    <RAW, T> T get(String key, Function<RAW, T> converter);

    void set(String key, Object value);

    <RAW, T> void set(String key, T value, Function<T, RAW> converter);

    void set(String key, MapSchema mapSchema);

    <RAW, T> void set(String key, MapSchemaList<RAW, T> mapSchemaList);

    <RAW, T> MapSchemaList<RAW, T> getList(String key);

    <RAW, T> MapSchemaList<RAW, T> getList(String key,
            Function<T, RAW> inputConverter, Function<RAW, T> outputConverter);

    <T> T computeIfAbsent(String key, Function<String, T> mappingFunction);

    List<String> getPath();
}
