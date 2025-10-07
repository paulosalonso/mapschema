package com.github.paulosalonso.mapschema;

import lombok.Getter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

public class MapSchemaList<RAW, T> implements List<T> {

    @Getter
    final List<RAW> source;
    final Function<T, RAW> inputConverter;
    final Function<RAW, T> outputConverter;

    public MapSchemaList() {
        this.source = new ArrayList<>();
        this.inputConverter = null;
        this.outputConverter = null;
    }

    public MapSchemaList(Function<T, RAW> inputConverter, Function<RAW, T> outputConverter) {
        this.source = new ArrayList<>();
        this.inputConverter = inputConverter;
        this.outputConverter = outputConverter;
    }

    public MapSchemaList(List<RAW> source) {
        this.source = source;
        this.inputConverter = null;
        this.outputConverter = null;
    }

    public MapSchemaList(List<RAW> source, Function<T, RAW> inputConverter, Function<RAW, T> outputConverter) {
        this.source = source;
        this.inputConverter = inputConverter;
        this.outputConverter = outputConverter;
    }

    public MapSchemaList(MapSchema parent, String key) {
        this.source = parent.computeIfAbsent(key, k -> new ArrayList<>());
        this.inputConverter = null;
        this.outputConverter = null;
    }

    public MapSchemaList(MapSchema parent, String key, Function<T, RAW> inputConverter, Function<RAW, T> outputConverter) {
        this.source = parent.computeIfAbsent(key, k -> new ArrayList<>());
        this.inputConverter = inputConverter;
        this.outputConverter = outputConverter;
    }

    private RAW convertInput(T value) {
        return inputConverter != null ? inputConverter.apply(value) : (RAW) value;
    }

    private T convertOutput(RAW rawValue) {
        return outputConverter != null ? outputConverter.apply(rawValue) : (T) rawValue;
    }

    @Override
    public T get(int index) {
        return convertOutput(source.get(index));
    }

    @Override
    public boolean add(T value) {
        return source.add(convertInput(value));
    }

    @Override
    public void add(int index, T value) {
        source.add(index, convertInput(value));
    }

    @Override
    public boolean addAll(Collection<? extends T> values) {
        return source.addAll(values.stream()
                .map(this::convertInput)
                .toList());
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> values) {
        return source.addAll(index, values.stream()
                .map(this::convertInput)
                .toList());
    }

    @Override
    public T set(int index, T value) {
        final var input = convertInput(value);
        return convertOutput(source.set(index, input));
    }

    @Override
    public T remove(int index) {
        final var value = source.remove(index);
        return convertOutput(value);
    }

    @Override
    public boolean remove(Object value) {
        try {
            return source.remove(convertInput((T) value));
        } catch (ClassCastException e) {
            return source.remove(value);
        }
    }

    @Override
    public boolean removeAll(Collection<?> values) {
        try {
            return source.removeAll(values.stream()
                    .map(item -> (T) item)
                    .map(this::convertInput)
                    .toList());
        } catch (ClassCastException e) {
            return source.removeAll(values);
        }
    }

    @Override
    public boolean retainAll(Collection<?> values) {
        try {
            return source.retainAll(values.stream()
                    .map(item -> (T) item)
                    .map(this::convertInput)
                    .toList());
        } catch (ClassCastException e) {
            return source.retainAll(values);
        }
    }

    @Override
    public boolean contains(Object value) {
        try {
            return source.contains(convertInput((T) value));
        } catch (ClassCastException e) {
            return source.contains(value);
        }
    }

    @Override
    public boolean containsAll(Collection<?> values) {
        try {
            return source.containsAll(values.stream()
                    .map(item -> (T) item)
                    .map(this::convertInput)
                    .toList());
        } catch (ClassCastException e) {
            return source.containsAll(values);
        }
    }

    @Override
    public int indexOf(Object value) {
        try {
            return source.indexOf(convertInput((T) value));
        } catch (ClassCastException e) {
            return source.indexOf(value);
        }
    }

    @Override
    public int lastIndexOf(Object value) {
        try {
            return source.lastIndexOf(convertInput((T) value));
        } catch (ClassCastException e) {
            return source.lastIndexOf(value);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return source.stream()
                .map(this::convertOutput)
                .toList()
                .iterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return source.stream()
                .map(this::convertOutput)
                .toList()
                .listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return source.stream()
                .skip(index)
                .map(this::convertOutput)
                .toList()
                .listIterator();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return source.subList(fromIndex, toIndex)
                .stream()
                .map(this::convertOutput)
                .toList();
    }

    @Override
    public void clear() {
        source.clear();
    }

    @Override
    public int size() {
        return source.size();
    }

    @Override
    public boolean isEmpty() {
        return source.isEmpty();
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public <E> E[] toArray(E[] array) {
        return source.toArray(array);
    }

    @Override
    public void forEach(Consumer<? super T> consumer) {
        source.forEach(value -> consumer.accept(convertOutput(value)));
    }

    @Override
    public Stream<T> stream() {
        return source.stream().map(this::convertOutput);
    }

    @Override
    public String toString() {
        return source.stream()
                .map(this::convertOutput)
                .toList()
                .toString();
    }
}
