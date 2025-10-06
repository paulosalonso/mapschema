package com.github.paulosalonso.mapschema;

import com.github.paulosalonso.mapschema.MapSchemaEntryList.EntryFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MapSchemaEntry implements MapSchema {

    @Getter
    protected MapSchema parent;

    protected String key;

    @Getter @Setter
    protected Map<String, Object> source;

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

    public <T extends MapSchema> void set(String key, MapSchemaEntryList<T> mapSchemaEntryList) {
        checkNotNull(key, "key");

        if (mapSchemaEntryList == null) {
            this.source.put(key, null);
        } else {
            this.source.put(key, mapSchemaEntryList.getSources());
        }
    }

    @Override
    public <T extends MapSchema> MapSchemaEntryList<T> getEntryList(String key, EntryFactory<T> entryFactory) {
        checkNotNull(key, "key");
        return MapSchemaEntryList.createKeepingSourceValues(this, key, entryFactory);
    }

    @Override
    public <RAW, T> MapSchemaRawList<RAW, T> getRawList(String key) {
        checkNotNull(key, "key");
        return new MapSchemaRawList<>(this, key);
    }

    @Override
    public <RAW, T> MapSchemaRawList<RAW, T> getRawList(String key,
            Function<T, RAW> inputConverter, Function<RAW, T> outputConverter) {

        checkNotNull(key, "key");
        return new MapSchemaRawList<>(this, key, inputConverter, outputConverter);
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
