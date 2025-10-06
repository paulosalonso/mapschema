package com.github.paulosalonso.mapschema;

import com.github.paulosalonso.mapschema.MapSchemaEntryList.EntryFactory;

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

    <T extends MapSchema> void set(String key, MapSchemaEntryList<T> mapSchemaEntryList);

    <T extends MapSchema> MapSchemaEntryList<T> getEntryList(String key, EntryFactory<T> entryFactory);

    <RAW, T> MapSchemaRawList<RAW, T> getRawList(String key);

    <RAW, T> MapSchemaRawList<RAW, T> getRawList(String key, 
        Function<T, RAW> inputConverter, Function<RAW, T> outputConverter);

    <T> T computeIfAbsent(String key, Function<String, T> mappingFunction);

    List<String> getPath();
}
