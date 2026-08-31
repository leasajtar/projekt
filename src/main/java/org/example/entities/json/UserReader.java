package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserReader {
    private UserReader() {}

    private static final Path USER_JSON = Paths.get("data/user.json");
    private static final Logger LOGGER = LoggerFactory.getLogger(UserReader.class);

    static void main() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try(BufferedReader userReader = Files.newBufferedReader(USER_JSON);
            Jsonb jsonb = JsonbBuilder.create(config);) {

            List<User> users = jsonb.fromJson(
                    userReader,
                    ArrayList.class
            );

            LOGGER.info("Lista korisnika: {}", users.size());
            for (User u : users) {
                LOGGER.info("{} - {}", u.getUsername(), u.getEmail());
            }

        } catch (Exception e) {
            LOGGER.error("Greška", e);
        }
    }

    public static List<User> readUsers() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try(BufferedReader userReader = Files.newBufferedReader(USER_JSON);
            Jsonb jsonb = JsonbBuilder.create(config);) {
            LOGGER.debug("Reading JSON from: {}", Paths.get("data/user.json").toAbsolutePath());


            return jsonb.fromJson(
                    userReader,
                    ArrayList.class
            );

        } catch (Exception e) {
            LOGGER.error("Error reading users", e);
            return new ArrayList<>();
        }
    }
}