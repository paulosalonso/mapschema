package com.github.paulosalonso.mapschema.example;

import com.github.paulosalonso.mapschema.MapSchema;
import com.github.paulosalonso.mapschema.MapSchemaEntry;
import com.github.paulosalonso.mapschema.MapSchemaEntryList;
import com.github.paulosalonso.mapschema.MapSchemaRoot;
import com.github.paulosalonso.mapschema.converter.LocalDateConverter;
import com.github.paulosalonso.mapschema.converter.UuidConverter;

import java.time.LocalDate;
import java.util.*;

public class Example {

    private static final RootExample ROOT = new RootExample() {{
        put("id", UUID.randomUUID().toString());

        put("descricao", "Exemplo de uso do MapSchema");

        put("entry", new HashMap<String, Object>() {{
            put("id", 9);
            put("descricao", "Objeto complexo");
        }});

        put("entryList", new ArrayList<>(){{
            add(new HashMap<String, Object>() {{
                put("id", 0);
                put("descricao", "Item 0 da lista de objetos complexos");
            }});

            add(new HashMap<String, Object>() {{
                put("id", 1);
                put("descricao", "Item 1 da lista de objetos complexos");
            }});
        }});

        put("rawList", new ArrayList<>() {{
            add("2025-10-04");
        }});
    }};

    public static void main(String[] args) {
        System.out.println("Estado inicial");
        System.out.println(ROOT);
        System.out.println();

        ROOT.setEntry(new EntryExample() {{
            setId(UUID.randomUUID());
            setDescricao("Objeto complexo inserido posteriormente");
        }});

        System.out.println("Setando entry");
        System.out.println(ROOT);
        System.out.println();

        ROOT.setEntryList(MapSchemaEntryList
                .createResetingSourceValues(ROOT, "entryList", EntryExample::new));

        System.out.println("Setando entryList removendo os itens existentes");
        System.out.println(ROOT);
        System.out.println();

        ROOT.getEntryList().add(new EntryExample() {{
            setId(UUID.randomUUID());
            setDescricao("Item inserido posteriormente na lista de objetos complexos");
        }});

        System.out.println("Incluindo entry em entryList");
        System.out.println(ROOT);
        System.out.println();

        ROOT.setEntryList(MapSchemaEntryList
                .createKeepingSourceValues(ROOT, "entryList", EntryExample::new));

        System.out.println("Setando entryList mantendo os itens existentes");
        System.out.println(ROOT);
        System.out.println();

        ROOT.getRawList().add(LocalDate.now());

        System.out.println("Incluindo item em rawList");
        System.out.println(ROOT);
    }

    static class RootExample extends MapSchemaRoot {

        private EntryExample entry;
        private List<EntryExample> entryList;
        private List<LocalDate> rawList;

        public UUID getId() {
            return super.get("id", UuidConverter::fromString);
        }

        public void setId(UUID id) {
            super.set("id", id, UuidConverter::toString);
        }

        public String getDescricao() {
            return super.get("descricao",Object::toString);
        }

        public EntryExample getEntry() {
            if (entry == null) {
                entry = super.<Map<String, Object>, EntryExample>get("entry", EntryExample::new);
            }

            return entry;
        }

        public void setEntry(EntryExample entry) {
            this.entry = entry;
            super.set("entry", entry);
        }

        public void setDescricao(String descricao) {
            super.set("descricao", descricao);
        }

        public List<EntryExample> getEntryList() {
            if (entryList == null) {
                entryList = super.getEntryList("entryList", EntryExample::new);
            }

            return entryList;
        }

        public void setEntryList(MapSchemaEntryList<EntryExample> entryList) {
            super.set("entryList", entryList);
            this.entryList = entryList;
        }

        public List<LocalDate> getRawList() {
            if (rawList == null) {
                rawList = super.getRawList("rawList", LocalDateConverter::toString, LocalDateConverter::fromString);
            }

            return rawList;
        }
    }

    static class EntryExample extends MapSchemaEntry {

        public EntryExample() {
            super();
        }

        public EntryExample(Map<String, Object> source) {
            super(source);
        }

        public EntryExample(MapSchema parent, String key, Map<String, Object> source) {
            super(parent, key, source);
        }

        public UUID getId() {
            return get("id", UuidConverter::fromString);
        }

        public void setId(UUID id) {
            super.set("id", id, UuidConverter::toString);
        }

        public String getDescricao() {
            return super.get("descricao", Object::toString);
        }

        public void setDescricao(String descricao) {
            super.set("descricao", descricao);
        }
    }
}
