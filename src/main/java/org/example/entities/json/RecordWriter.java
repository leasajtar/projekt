package org.example.entities.json;

import jakarta.json.bind.*;

import java.lang.Record;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

public class RecordWriter {
    public static void writeRecords(Set<Record> records) {
        try {
            Jsonb jsonb = JsonbBuilder.create();

            String json = jsonb.toJson(records);
            Files.writeString(Paths.get("data/record.json"), json);

            System.out.println("✅ Record data successfully saved to record.json!");

        } catch (Exception e) {
            System.err.println("Error writing booking data: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
