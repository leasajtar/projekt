package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/** Čita stavke (vrste događaja) iz {@code data/item.json}. */
public class ItemReader {
    private ItemReader() {}

    private static final Path JSON_LISTA = Paths.get("data/item.json");
    private static final Logger LOGGER =
            LoggerFactory.getLogger(ItemReader.class);

    /** Učitava stavke i ispisuje ih u log. */
    static void main() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try(BufferedReader itemPath = Files.newBufferedReader(JSON_LISTA);
            Jsonb jsonb = JsonbBuilder.create(config);) {

            List<Item> items = jsonb.fromJson(itemPath,ArrayList.class);

            LOGGER.info("Lista itema: {}", items.size());
            for (Item item : items) {
                LOGGER.info("{} -> {}", item.getEventType(), item.getPrice());
            }

        } catch (IOException e) {
            LOGGER.error("Greška pri čitanju datoteke", e);
        } catch (Exception e) {
            LOGGER.error("Pogreška", e);
        }
    }

    /**
     * Učitava stavke iz {@code data/item.json}.
     *
     * @return lista stavki, ili prazna lista ako dođe do greške
     */
    public static List<Item> readItems() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try(BufferedReader itemPath = Files.newBufferedReader(JSON_LISTA);
                Jsonb jsonb = JsonbBuilder.create(config);) {
            return jsonb.fromJson(itemPath,ArrayList.class);

        } catch (Exception e) {
            LOGGER.error("Error reading items", e);
            return new ArrayList<>();
        }
    }
}