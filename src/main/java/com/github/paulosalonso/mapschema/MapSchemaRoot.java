package com.github.paulosalonso.mapschema;

import com.github.paulosalonso.mapschema.MapSchemaEntryList.EntryFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapSchemaRoot extends HashMap<String, Object> implements MapSchema {

    protected final MapSchema root = new MapSchemaEntry(this);

    @Override
    public MapSchema getParent() {
        throw new UnsupportedOperationException("MapSchemaRoot has no parent");
    }

    @Override
    public void setParent(MapSchema parent, String key) {
        throw new UnsupportedOperationException("MapSchemaRoot has no parent");
    }

    @Override
    public Map<String, Object> getSource() {
        return this;
    }

    @Override
    public void setSource(Map<String, Object> source) {
        throw new UnsupportedOperationException("MapSchemaRoot has no parent");
    }

    @Override
    public Object get(String key) {
        return root.get(key);
    }

    @Override
    public <RAW, T> T get(String key, Function<RAW, T> converter) {
        return root.get(key, converter);
    }

    @Override
    public void set(String key, Object value) {
        root.set(key, value);
    }

    @Override
    public <RAW, T> void set(String key, T value, Function<T, RAW> converter) {
        root.set(key, value, converter);
    }

    @Override
    public void set(String key, MapSchema mapSchema) {
        root.set(key, mapSchema);
    }

    @Override
    public <T extends MapSchema> void set(String key, MapSchemaEntryList<T> mapSchemaEntryList) {
        root.set(key, mapSchemaEntryList);
    }

    @Override
    public <T extends MapSchema> MapSchemaEntryList<T> getEntryList(String key, EntryFactory<T> entryFactory) {
        return root.getEntryList(key, entryFactory);
    }

    @Override
    public <RAW, T> MapSchemaRawList<RAW, T> getRawList(String key) {
        return root.getRawList(key);
    }

    @Override
    public <RAW, T> MapSchemaRawList<RAW, T> getRawList(String key, Function<T, RAW> inputConverter, Function<RAW, T> outputConverter) {
        return root.getRawList(key, inputConverter, outputConverter);
    }

    @Override
    public <T> T computeIfAbsent(String key, Function<String, T> mappingFunction) {
        return root.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public List<String> getPath() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
