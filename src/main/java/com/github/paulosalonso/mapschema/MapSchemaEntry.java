package com.github.paulosalonso.mapschema;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapSchemaEntry implements MapSchema {

    @Getter
    MapSchema parent;

    String key;

    @Getter @Setter
    Map<String, Object> source;

    /**
     * Default constructor
     */
    public MapSchemaEntry() {
        this.source = new HashMap<>();
    }

    /**
     * Constructor for root instances
     *
     * @param source Map that represents the root source of data
     */
    public MapSchemaEntry(Map<String, Object> source) {
        checkNotNull(source, "source");
        this.parent = null;
        this.key = null;
        this.source = source;
    }

    /**
     * Constructor for child instances
     *
     * @param parent Instance that contains the key that represents this child
     * @param key Key in the parent that represents the source data of this child
     */
    public MapSchemaEntry(MapSchema parent, String key) {
        checkNotNull(parent, "parent");
        checkNotNull(key, "key");

        this.parent = parent;
        this.key = key;
        this.source = parent.computeIfAbsent(key, k -> new HashMap<>());
    }

    /**
     * Constructor for child instances that will be on a list
     *
     * @param parent Instance that contains the key that represents this list of children
     * @param key Key in the parent that represents the source data of this list of children
     * @param source Map that represents the source of data of this child
     */
    public MapSchemaEntry(MapSchema parent, String key, Map<String, Object> source) {
        checkNotNull(parent, "parent");
        checkNotNull(key, "key");
        checkNotNull(source, "source");

        this.parent = parent;
        this.key = key;
        this.source = source;
    }

    private void checkNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(String.format("Argument %s must be not null", name));
        }
    }

    @Override
    public void setParent(MapSchema parent, String key) {
        checkNotNull(parent, "parent");
        checkNotNull(key, "key");
        this.parent = parent;
        this.key = key;
        parent.getSource().put(key, this.source);
    }

    @Override
    public Object get(String key) {
        checkNotNull(key, "key");
        return source.get(key);
    }

    @Override
    public <RAW, T> T get(String key, Function<RAW, T> converter) {
        checkNotNull(key, "key");
        final var value = source.get(key);
        return value == null ? null : converter.apply((RAW) value);
    }

    @Override
    public void set(String key, Object value) {
        checkNotNull(key, "key");
        source.put(key, value);
    }

    @Override
    public <RAW, T> void set(String key, T value, Function<T, RAW> converter) {
        checkNotNull(key, "key");

        if (value == null) {
            source.put(key, null);
        } else {
            source.put(key, converter.apply(value));
        }
    }

    @Override
    public void set(String key, MapSchema mapSchema) {
        checkNotNull(key, "key");

        if (mapSchema == null) {
            this.source.put(key, null);
        } else {
            this.source.put(key, mapSchema.getSource());
            mapSchema.setParent(this, key);
        }
    }

    @Override
    public <RAW, T> void set(String key, MapSchemaList<RAW, T> mapSchemaList) {
        checkNotNull(key, "key");

        if (mapSchemaList == null) {
            this.source.put(key, null);
        } else {
            this.source.put(key, mapSchemaList.getSource());
        }
    }

    @Override
    public <RAW, T> MapSchemaList<RAW, T> getList(String key) {
        checkNotNull(key, "key");
        return new MapSchemaList<>(this, key);
    }

    @Override
    public <RAW, T> MapSchemaList<RAW, T> getList(String key,
            Function<T, RAW> inputConverter, Function<RAW, T> outputConverter) {

        checkNotNull(key, "key");
        return new MapSchemaList<>(this, key, inputConverter, outputConverter);
    }

    @Override
    public <T> T computeIfAbsent(String key, Function<String, T> mappingFunction) {
        return (T) source.computeIfAbsent(key, mappingFunction);
    }

    @Override
    public List<String> getPath() {
        return new ArrayList<>(parent.getPath()) {{
            add(key);
        }};
    }

    @Override
    public String toString() {
        return this.source.toString();
    }
}
