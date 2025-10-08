package com.github.paulosalonso.mapschema.entity;

import com.github.paulosalonso.mapschema.MapSchemaEntry;
import com.github.paulosalonso.mapschema.converter.UuidConverter;

import java.util.Map;
import java.util.UUID;

public class EntryEntity extends MapSchemaEntry {

    public EntryEntity() {
        super();
    }

    public EntryEntity(Map<String, Object> source) {
        super(source);
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