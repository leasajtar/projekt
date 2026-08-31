package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.Record;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RecordReader {
    public static ArrayList<org.example.entities.Record> readRecords() {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String json = Files.readString(Paths.get("data/record.json"));

            return jsonb.fromJson(
                    json,
                    new ArrayList<Record>(){}.getClass().getGenericSuperclass()
            );

        } catch (Exception e) {
            return new ArrayList<>(); // return empty list if file missing
        }
    }
}
