package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserReader {
    private static final Logger logger = LoggerFactory.getLogger(UserReader.class);

    public static void main(String[] args) {
        try {
            Jsonb jsonb = JsonbBuilder.create();
            String jsonLista = Files.readString(Paths.get("data/user.json"));

            List<User> users = jsonb.fromJson(
                    jsonLista,
                    new ArrayList<User>(){}.getClass().getGenericSuperclass()
            );

            logger.info("Lista korisnika: {}", users.size());
            for (User u : users) {
                logger.info("{} - {}", u.getUsername(), u.getEmail());
            }

        } catch (Exception e) {
            logger.error("Greška", e);
        }
    }

    public static List<User> readUsers() {
        try {
            logger.debug("Reading JSON from: {}", Paths.get("data/user.json").toAbsolutePath());

            Jsonb jsonb = JsonbBuilder.create();
            String json = Files.readString(Paths.get("data/user.json"));

            return jsonb.fromJson(
                    json,
                    new ArrayList<User>(){}.getClass().getGenericSuperclass()
            );

        } catch (Exception e) {
            logger.error("Error reading users", e);
            return new ArrayList<>();
        }
    }
}