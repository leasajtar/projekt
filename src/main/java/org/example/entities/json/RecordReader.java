package org.example.entities.json;

import jakarta.json.bind.*;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class RecordReader {
    private RecordReader() {}

    private static final Path RECORD_PATH = Paths.get("data/record.json");

    public static List<Record> readRecords() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try (BufferedReader recordReader = Files.newBufferedReader(RECORD_PATH);
             Jsonb jsonb = JsonbBuilder.create(config);) {

            return jsonb.fromJson(
                        recordReader,
                        ArrayList.class
            );

        } catch (Exception _) {
            return new ArrayList<>();
        }
    }
}
