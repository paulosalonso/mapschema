package com.github.paulosalonso.mapschema;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.paulosalonso.mapschema.entity.RootEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static org.assertj.core.api.Assertions.assertThat;

public class PerformanceTest {

    private static final ObjectMapper MAPPER = new ObjectMapper() {{
        registerModules(new JavaTimeModule());
        disable(WRITE_DATES_AS_TIMESTAMPS);
    }};

    @BeforeAll
    static void warmUp() throws IOException {
        System.out.println("Warming up the ObjectMapper");
        MAPPER.readValue(PerformanceTest.class.getResourceAsStream("/source.json"), RootEntity.class);
        MAPPER.readValue(PerformanceTest.class.getResourceAsStream("/source.json"), Root.class);
    }

    @Test
    void shouldMapFasterToMapSchemaThanToPojo() throws IOException {
        System.out.println("Comparing the mapping time to map 10.000 objects to MapSchema and to regular POJO");

        final var mapSchemaStart = System.currentTimeMillis();
        loadAsMapSchema();
        final var mapSchemaTime = System.currentTimeMillis() - mapSchemaStart;

        System.out.printf("Mapped to MapSchema in %d ms\n", mapSchemaTime);

        final var pojoStart = System.currentTimeMillis();
        loadAsPojo();
        final var pojoTime = System.currentTimeMillis() - pojoStart;

        System.out.printf("Mapped to POJO in %d ms\n", pojoTime);

        assertThat(mapSchemaTime).isLessThan(pojoTime);

        final var percent = (((double) (pojoTime - mapSchemaTime)) / pojoTime) * 100;

        System.out.printf("MapSchema is %.1f%% faster", percent);
    }

    private static List<RootEntity> loadAsMapSchema() throws IOException {
        return MAPPER.readValue(
                MapSchemaTest.class.getResourceAsStream("/sources.json"), new TypeReference<List<RootEntity>>() {});
    }

    private static List<Root> loadAsPojo() throws IOException {
        return MAPPER.readValue(
                MapSchemaTest.class.getResourceAsStream("/sources.json"), new TypeReference<List<Root>>() {});
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class Root {
        private String id;
        private String descricao;
        private Entry entry;
        private List<Entry> entryList;
        private List<LocalDate> convertedRawList;
        private List<String> rawList;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    static class Entry {
        private String id;
        private String descricao;
    }
}
