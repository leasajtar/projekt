package org.example.enteties.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.enteties.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ItemWriter {

    private static final Path ITEM_PATH = Paths.get("data/item.json");

    public static void main(String[] args) {
        try {
            Jsonb jsonb = JsonbBuilder.create();

            List<Item> items = Arrays.asList(
                    new Item(1,"BIRTHDAY", new java.math.BigDecimal("200")),
                    new Item(2,"WEDDING", new java.math.BigDecimal("700")),
                    new Item(3,"FUNERAL", new java.math.BigDecimal("300")),
                    new Item(4,"CONCERT", new java.math.BigDecimal("500")),
                    new Item(5,"SPORT", new java.math.BigDecimal("100"))
            );

            writeItems(items);
            System.out.println("\n✅ Podaci uspješno zapisani u JSON!");

        } catch (Exception e) {
            System.err.println("Pogreška: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void writeItems(List<Item> items) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(items);
        Files.createDirectories(ITEM_PATH.getParent());
        Files.writeString(ITEM_PATH, json);
    }
}
