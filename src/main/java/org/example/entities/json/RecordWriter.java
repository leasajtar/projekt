package org.example.entities.json;

import jakarta.json.bind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class RecordWriter {
    private RecordWriter() {}
    private static final Logger LOGGER = LoggerFactory.getLogger(RecordWriter.class);
    private static final Path RECORD_PATH = Paths.get("data/record.json");

    public static void writeRecords(Set<Record> records) {
        try(Jsonb jsonb = JsonbBuilder.create();
            BufferedWriter recordWriter = Files.newBufferedWriter(RECORD_PATH);) {

            jsonb.toJson(records, recordWriter);

            LOGGER.info("Record data successfully saved to record.json");
        } catch (Exception e) {
            LOGGER.error("Error writing record data", e);
        }
    }
}