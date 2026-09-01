package org.example.entities.json;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import org.example.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/** Zapisuje stavke (vrste događaja) u {@code data/item.json}. */
public class ItemWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemWriter.class);
    private static final Path ITEM_PATH = Paths.get("data/item.json");

    /** Sprema pet unaprijed definiranih stavki. */
    static void main() {
        try {
            List<Item> items = Arrays.asList(
                    new Item(1,"BIRTHDAY", new java.math.BigDecimal("200")),
                    new Item(2,"WEDDING", new java.math.BigDecimal("700")),
                    new Item(3,"FUNERAL", new java.math.BigDecimal("300")),
                    new Item(4,"CONCERT", new java.math.BigDecimal("500")),
                    new Item(5,"SPORT", new java.math.BigDecimal("100"))
            );

            writeItems(items);
            LOGGER.info("Podaci uspješno zapisani u JSON!");

        } catch (Exception e) {
            LOGGER.error("Pogreška", e);
        }
    }

    /**
     * Sprema listu stavki u {@code data/item.json}.
     *
     * @param items stavke koje treba zapisati
     * @throws IOException ako zapisivanje u datoteku ne uspije
     */
    public static void writeItems(List<Item> items) throws IOException {
        try(Jsonb jsonb = JsonbBuilder.create();
            BufferedWriter writer = Files.newBufferedWriter(ITEM_PATH)) {
            jsonb.toJson(items, writer);
        }catch (Exception e){
            LOGGER.error("Error writing item data", e);
        }


    }
}