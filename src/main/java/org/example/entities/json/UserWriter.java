package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/** Zapisuje korisnike u {@code data/user.json}. */
public class UserWriter {
    private UserWriter() {}
    private static final Logger LOGGER = LoggerFactory.getLogger(UserWriter.class);
    private static final Path USER_PATH = Paths.get("data/user.json");

    /**
     * Sprema listu korisnika u {@code data/user.json}.
     *
     * @param users korisnici koje treba zapisati
     * @throws IOException ako zapisivanje u datoteku ne uspije
     */
    public static void writeUsers(List<User> users) throws IOException {
        try(Jsonb jsonb = JsonbBuilder.create();
            BufferedWriter writer = Files.newBufferedWriter(USER_PATH)) {
            jsonb.toJson(users, writer);
        }catch (Exception e){
            LOGGER.error("Error writing user data", e);
        }
    }
}