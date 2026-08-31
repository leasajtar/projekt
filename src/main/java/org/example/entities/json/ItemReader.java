package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ItemReader {
    private static final Logger logger = LoggerFactory.getLogger(ItemReader.class);

    public static void main(String[] args) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String jsonLista = Files.readString(Paths.get("data/item.json"));

            List<Item> items = jsonb.fromJson(
                    jsonLista,
                    new ArrayList<Item>(){}.getClass().getGenericSuperclass()
            );

            logger.info("Lista itema: {}", items.size());
            for (Item item : items) {
                logger.info("{} -> {}", item.getEventType(), item.getPrice());
            }

        } catch (IOException e) {
            logger.error("Greška pri čitanju datoteke", e);
        } catch (Exception e) {
            logger.error("Pogreška", e);
        }
    }

    public static List<Item> readItems() {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String json = Files.readString(Paths.get("data/item.json"));

            return jsonb.fromJson(
                    json,
                    new ArrayList<Item>(){}.getClass().getGenericSuperclass()
            );

        } catch (Exception e) {
            logger.error("Error reading items", e);
            return new ArrayList<>();
        }
    }
}