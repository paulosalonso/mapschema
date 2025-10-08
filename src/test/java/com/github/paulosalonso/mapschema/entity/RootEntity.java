package com.github.paulosalonso.mapschema.entity;

import com.github.paulosalonso.mapschema.MapSchemaList;
import com.github.paulosalonso.mapschema.MapSchemaRoot;
import com.github.paulosalonso.mapschema.converter.LocalDateConverter;
import com.github.paulosalonso.mapschema.converter.MapSchemaConverter;
import com.github.paulosalonso.mapschema.converter.UuidConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RootEntity extends MapSchemaRoot {

    private EntryEntity entry;
    private List<EntryEntity> entryList;
    private List<LocalDate> convertedList;
    private List<String> rawList;

    public UUID getId() {
        return super.get("id", UuidConverter::fromString);
    }

    public void setId(UUID id) {
        super.set("id", id, UuidConverter::toString);
    }

    public String getDescricao() {
        return super.get("descricao",Object::toString);
    }

    public void setDescricao(String descricao) {
        super.set("descricao", descricao);
    }

    public EntryEntity getEntry() {
        if (entry == null) {
            entry = super.<Map<String, Object>, EntryEntity>get("entry", EntryEntity::new);
        }

        return entry;
    }

    public void setEntry(EntryEntity entry) {
        this.entry = entry;
        super.set("entry", entry);
    }

    public List<EntryEntity> getEntryList() {
        if (entryList == null) {
            entryList = super.getList("entryList",
                    MapSchemaConverter::toSource, MapSchemaConverter.toMapSchema(EntryEntity::new));
        }

        return entryList;
    }

    public void setEntryList(MapSchemaList<Map<String, Object>, EntryEntity> entryList) {
        super.set("entryList", entryList);
        this.entryList = entryList;
    }

    public List<LocalDate> getConvertedList() {
        if (convertedList == null) {
            convertedList = super.getList("convertedRawList",
                    LocalDateConverter::toString, LocalDateConverter::fromString);
        }

        return convertedList;
    }

    public void setConvertedList(MapSchemaList<String, LocalDate> convertedList) {
        super.set("convertedRawList", convertedList);
        this.convertedList = convertedList;
    }

    public List<String> getRawList() {
        if (rawList == null) {
            rawList = super.getList("rawList");
        }

        return rawList;
    }

    public void setRawList(MapSchemaList<String, String> rawList) {
        super.set("rawList", rawList);
        this.rawList = rawList;
    }
}