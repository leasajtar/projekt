package org.example.enteties.json;

import jakarta.json.bind.*;
import org.example.enteties.Record;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RecordReader {
    public static ArrayList<org.example.enteties.Record> readRecords() {
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
