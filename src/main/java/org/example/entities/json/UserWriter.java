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
import java.util.Arrays;
import java.util.List;

/** Zapisuje korisnike u {@code data/user.json}. */
public class UserWriter {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserWriter.class);
    private static final Path USER_PATH = Paths.get("data/user.json");

    /** Sprema tri unaprijed definirana korisnika. */
    static void main() {
        JsonbConfig config = new JsonbConfig()
                .withFormatting(true);
        try (Jsonb jsonb = JsonbBuilder.create(config);) {

            List<User> users = Arrays.asList(
                    new User.UserBuilder(1, "Marko123", "BoZiCjEnAj6655").email("marko.m@gmail.com").phone("0966465258").build(),
                    new User.UserBuilder(2, "SARAAAAA", "Fdiy*UHUg76F&(").email("sara.marul@gmail.com").build(),
                    new User.UserBuilder(3, "devito?", "idenAmaX586").email("devito.business@yahoo.com").phone("[phone protected]").build()
            );

            String jsonLista = jsonb.toJson(users);
            Files.writeString(Paths.get("data/user.json"), jsonLista);

            LOGGER.info("USER podaci zapisani u JSON!");

        } catch (Exception e) {
            LOGGER.error("Pogreška", e);
        }
    }

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