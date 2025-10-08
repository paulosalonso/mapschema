package com.github.paulosalonso.mapschema;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.paulosalonso.mapschema.converter.LocalDateConverter;
import com.github.paulosalonso.mapschema.entity.EntryEntity;
import com.github.paulosalonso.mapschema.entity.RootEntity;
import lombok.SneakyThrows;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.assertj.core.api.Assertions.assertThat;

public class MapSchemaTest {

    private static final ObjectMapper MAPPER = new ObjectMapper() {{
        registerModules(new JavaTimeModule());
        disable(WRITE_DATES_AS_TIMESTAMPS);
    }};

    @SneakyThrows
    private RootEntity load() {
        final var stream = MapSchemaTest.class.getResourceAsStream("/source.json");
        return MAPPER.readValue(stream, RootEntity.class);
    }

    @Test
    void shouldMapAJsonToAMapSchemaEntity() {
        final var mapSchemaEntity = load();

        assertThat(mapSchemaEntity.getId()).isEqualTo(UUID.fromString("43db548c-7c68-4da5-8377-dfe76df2787a"));
        assertThat(mapSchemaEntity.getDescricao()).isEqualTo("KAM");

        assertThat(mapSchemaEntity.getEntry())
                .isNotNull()
                .satisfies(entry -> {
                    assertThat(entry.getId()).isEqualTo(UUID.fromString("282003e6-73aa-44f6-90d3-ebee3b3bd5a1"));
                    assertThat(entry.getDescricao()).isEqualTo("WPVMHYQXL");
                });

        assertThat(mapSchemaEntity.getEntryList())
                .hasSize(2)
                .satisfies(entryList -> {
                    final var entry0 = entryList.get(0);
                    assertThat(entry0.getId()).isEqualTo(UUID.fromString("282003e6-73aa-44f6-90d3-ebee3b3bd5a1"));
                    assertThat(entry0.getDescricao()).isEqualTo("XKVF");

                    final var entry1 = entryList.get(1);
                    assertThat(entry1.getId()).isEqualTo(UUID.fromString("282003e6-73aa-44f6-90d3-ebee3b3bd5a1"));
                    assertThat(entry1.getDescricao()).isEqualTo("ZFNDYP");
                });

        assertThat(mapSchemaEntity.getConvertedList())
                .hasSize(4)
                .satisfies(convertedList -> {
                    assertThat(convertedList.get(0)).isEqualTo(LocalDate.of(2082, 10, 11));
                    assertThat(convertedList.get(1)).isEqualTo(LocalDate.of(2035, 9, 11));
                    assertThat(convertedList.get(2)).isEqualTo(LocalDate.of(1972, 5, 14));
                    assertThat(convertedList.get(3)).isEqualTo(LocalDate.of(1987, 12, 3));
                });

        assertThat(mapSchemaEntity.getRawList())
                .hasSize(2)
                .satisfies(rawList -> {
                    assertThat(rawList.get(0)).isEqualTo("KRSD");
                    assertThat(rawList.get(1)).isEqualTo("KDTDT");
                });
    }

    @Test
    void shouldCreateAMapSchemaEntityFromScratch() {
        final var rootId = UUID.randomUUID();
        final var rootDescricao = Instancio.create(String.class);

        final var entryId = UUID.randomUUID();
        final var entryDescricao = Instancio.create(String.class);

        final var entry0Id = UUID.randomUUID();
        final var entry0Descricao = Instancio.create(String.class);
        final var entry1Id = UUID.randomUUID();
        final var entry1Descricao = Instancio.create(String.class);
        final var entry2Id = UUID.randomUUID();
        final var entry2Descricao = Instancio.create(String.class);

        final var localDate0 = Instancio.create(LocalDate.class);
        final var localDate1 = Instancio.create(LocalDate.class);

        final var raw0 = Instancio.create(String.class);
        final var raw1 = Instancio.create(String.class);
        final var raw2 = Instancio.create(String.class);
        final var raw3 = Instancio.create(String.class);

        final var mapSchemaEntity = new RootEntity() {{
            setId(rootId);
            setDescricao(rootDescricao);
            setEntry(new EntryEntity() {{
                setId(entryId);
                setDescricao(entryDescricao);
            }});
            setEntryList(new MapSchemaList<>() {{
                add(new EntryEntity() {{
                    setId(entry0Id);
                    setDescricao(entry0Descricao);
                }});
                add(new EntryEntity() {{
                    setId(entry1Id);
                    setDescricao(entry1Descricao);
                }});
                add(new EntryEntity() {{
                    setId(entry2Id);
                    setDescricao(entry2Descricao);
                }});
            }});
            setConvertedList(new MapSchemaList<>(LocalDateConverter::toString, LocalDateConverter::fromString) {{
                add(localDate0);
                add(localDate1);
            }});
            setRawList(new MapSchemaList<>() {{
                add(raw0);
                add(raw1);
                add(raw2);
                add(raw3);
            }});
        }};

        assertThat(mapSchemaEntity.getId()).isEqualTo(rootId);
        assertThat(mapSchemaEntity.getDescricao()).isEqualTo(rootDescricao);

        assertThat(mapSchemaEntity.getEntry())
                .isNotNull()
                .satisfies(entry -> {
                    assertThat(entry.getId()).isEqualTo(entryId);
                    assertThat(entry.getDescricao()).isEqualTo(entryDescricao);
                });

        assertThat(mapSchemaEntity.getEntryList())
                .hasSize(3)
                .satisfies(entryList -> {
                    final var entry0 = entryList.get(0);
                    assertThat(entry0.getId()).isEqualTo(entry0Id);
                    assertThat(entry0.getDescricao()).isEqualTo(entry0Descricao);

                    final var entry1 = entryList.get(1);
                    assertThat(entry1.getId()).isEqualTo(entry1Id);
                    assertThat(entry1.getDescricao()).isEqualTo(entry1Descricao);

                    final var entry2 = entryList.get(2);
                    assertThat(entry2.getId()).isEqualTo(entry2Id);
                    assertThat(entry2.getDescricao()).isEqualTo(entry2Descricao);
                });

        assertThat(mapSchemaEntity.getConvertedList())
                .hasSize(2)
                .satisfies(convertedList -> {
                    assertThat(convertedList.get(0)).isEqualTo(localDate0);
                    assertThat(convertedList.get(1)).isEqualTo(localDate1);
                });

        assertThat(mapSchemaEntity.getRawList())
                .hasSize(4)
                .satisfies(rawList -> {
                    assertThat(rawList.get(0)).isEqualTo(raw0);
                    assertThat(rawList.get(1)).isEqualTo(raw1);
                    assertThat(rawList.get(2)).isEqualTo(raw2);
                    assertThat(rawList.get(3)).isEqualTo(raw3);
                });
    }
}
