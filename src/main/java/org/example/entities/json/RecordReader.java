package org.example.entities.json;

import jakarta.json.bind.*;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Čita povijesne zapise ({@link org.example.entities.Record}) iz {@code data/record.json}. */
public class RecordReader {
    private RecordReader() {}

    private static final Path RECORD_PATH = Paths.get("data/record.json");

    /**
     * Učitava zapise iz {@code data/record.json}.
     *
     * @return lista zapisa, ili prazna lista ako datoteka ne postoji ili dođe do greške
     */
    public static List<Record> readRecords() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try (BufferedReader recordReader = Files.newBufferedReader(RECORD_PATH);
             Jsonb jsonb = JsonbBuilder.create(config);) {

            return jsonb.fromJson(recordReader,ArrayList.class);

        } catch (Exception _) {
            return new ArrayList<>();
        }
    }
}
