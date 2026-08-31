package org.example.enteties.json;

import jakarta.json.bind.*;
import org.example.enteties.Item;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ItemReader {
    public static void main(String[] args) {
        try {
            Jsonb jsonb = JsonbBuilder.create();

            String jsonLista = Files.readString(Paths.get("data/item.json"));

            List<Item> items = jsonb.fromJson(
                    jsonLista,
                    new ArrayList<Item>(){}.getClass().getGenericSuperclass()
            );

            System.out.println("Lista itema:");
            for (Item item : items) {
                System.out.println(item.getEventType() + " → " + item.getPrice());
            }

        } catch (IOException e) {
            System.err.println("Greška pri čitanju datoteke: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Pogreška: " + e.getMessage());
            e.printStackTrace();
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
            System.err.println("Error reading items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // return empty list if file missing
        }
    }
}