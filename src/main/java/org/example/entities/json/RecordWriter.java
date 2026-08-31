package org.example.entities.json;

import jakarta.json.bind.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.Record;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class RecordWriter {
    private static final Logger logger = LoggerFactory.getLogger(RecordWriter.class);

    public static void writeRecords(Set<Record> records) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String json = jsonb.toJson(records);
            Files.writeString(Paths.get("data/record.json"), json);

            logger.info("Record data successfully saved to record.json");
        } catch (Exception e) {
            logger.error("Error writing record data", e);
        }
    }
}