package com.github.paulosalonso.mapschema;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MapSchemaEntryList<T extends MapSchema> implements List<T> {

    private final MapSchema parent;
    private final String key;

    @Getter
    private final List<Map<String, Object>> sources;

    private final EntryFactory<T> entryFactory;
    private final List<T> elements;

    /**
     * Constructor that clears the parent's value for key
     *
     * @param key The key of the source list in parent
     * @param parent MapSchema that contains the source list
     * @param entryFactory A factory that creates instances of MapSchemaEntry based on the source entries
     */
    private MapSchemaEntryList(String key, MapSchema parent, EntryFactory<T> entryFactory) {
        this.parent = parent;
        this.parent.set(key, this.sources = new ArrayList<>());
        this.key = key;
        this.entryFactory = entryFactory;
        this.elements = new ArrayList<>();
    }

    /**
     * Constructor that keeps the parent's value for key
     *
     * @param parent MapSchema that contains the source list
     * @param key The key of the source list in parent
     * @param entryFactory A factory that creates instances of MapSchemaEntry based on the source entries
     */
    private MapSchemaEntryList(MapSchema parent, String key, EntryFactory<T> entryFactory) {
        this.parent = parent;
        this.key = key;
        this.sources = parent.computeIfAbsent(key, k -> new ArrayList<>());
        this.entryFactory = entryFactory;
        this.elements = new ArrayList<>(Collections.nCopies(sources.size(), null));
    }

    public static <T extends MapSchema> MapSchemaEntryList<T>
            createResetingSourceValues(MapSchema parent, String key, EntryFactory<T> entryFactory) {

        return new MapSchemaEntryList<>(key, parent, entryFactory);
    }

    public static <T extends MapSchema> MapSchemaEntryList<T>
            createKeepingSourceValues(MapSchema parent, String key, EntryFactory<T> entryFactory) {

        return new MapSchemaEntryList<>(parent, key, entryFactory);
    }

    @Override
    public T get(int index) {
        var value = elements.get(index);

        if (value == null) {
            elements.set(index, value = entryFactory.create(parent, key, sources.get(index)));
        }

        return value;
    }

    @Override
    public boolean add(T value) {
        elements.add(value);
        sources.add(value.getSource());
        return true;
    }

    @Override
    public void add(int index, T value) {
        sources.add(index, value.getSource());
        elements.add(index, value);
    }

    @Override
    public boolean addAll(Collection<? extends T> values) {
        final var sources = values.stream()
                .map(MapSchema::getSource)
                .toList();

        this.elements.addAll(values);
        this.sources.addAll(sources);

        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> values) {
        final var sources = values.stream()
                .map(MapSchema::getSource)
                .toList();

        this.elements.addAll(index, values);
        this.sources.addAll(index, sources);

        return true;
    }

    @Override
    public T set(int index, T values) {
        sources.set(index, values.getSource());
        return elements.set(index, values);
    }

    public T remove(int index) {
        sources.remove(index);
        return elements.remove(index);
    }

    @Override
    public boolean remove(Object value) {
        final var index = elements.indexOf(value);

        if (index == -1) {
            return false;
        }

        sources.remove(index);
        elements.remove(index);

        return true;
    }

    @Override
    public boolean removeAll(Collection<?> values) {
        final var sources = values.stream()
                .map(item -> (MapSchema) item)
                .map(MapSchema::getSource)
                .toList();

        this.elements.removeAll(values);
        this.sources.removeAll(sources);

        return true;
    }

    @Override
    public boolean retainAll(Collection<?> values) {
        final var sources = this.elements.stream()
                .map(item -> (MapSchema) item)
                .map(MapSchema::getSource)
                .toList();

        this.elements.retainAll(values);
        this.sources.retainAll(sources);

        return true;
    }

    @Override
    public boolean contains(Object value) {
        return elements.contains(value);
    }

    @Override
    public boolean containsAll(Collection<?> values) {
        return this.elements.containsAll(values);
    }

    @Override
    public int indexOf(Object value) {
        return elements.indexOf(value);
    }

    @Override
    public int lastIndexOf(Object value) {
        return elements.lastIndexOf(value);
    }

    @Override
    public Iterator<T> iterator() {
        return elements.iterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return elements.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return elements.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return elements.subList(fromIndex, toIndex);
    }

    @Override
    public void clear() {
        elements.clear();
        sources.clear();
    }

    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @Override
    public <E> E[] toArray(E[] array) {
        return elements.toArray(array);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        elements.forEach(consumer);
    }

    public Stream<T> stream() {
        return elements.stream();
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @FunctionalInterface
    public interface EntryFactory<T extends MapSchema> {
        T create(MapSchema parent, String key, Map<String, Object> source);
    }
}
