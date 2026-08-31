package org.example.entities.json;

import jakarta.json.bind.*;
import org.example.entities.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class UserWriter {
    public static void main(String[] args) {
        try {
            Jsonb jsonb = JsonbBuilder.create();

            List<User> users = Arrays.asList(
                    new User.UserBuilder(1, "Marko123", "BoZiCjEnAj6655").email("marko.m@gmail.com").phone("0966465258").build(),
                    new User.UserBuilder(2, "SARAAAAA", "Fdiy*UHUg76F&(").email("sara.marul@gmail.com").build(),
                    new User.UserBuilder(3, "devito?", "idenAmaX586").email("devito.business@yahoo.com").phone("[phone protected]").build()
            );

            String jsonLista = jsonb.toJson(users);
            Files.writeString(Paths.get("data/user.json"), jsonLista);

            System.out.println("✅ USER podaci zapisani u JSON!");

        } catch (Exception e) {
            System.err.println("Pogreška: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static final Path USER_PATH = Paths.get("data/user.json");

    public static void writeUsers(List<User> users) throws IOException {
        Jsonb jsonb = JsonbBuilder.create();
        String json = jsonb.toJson(users);
        Files.createDirectories(USER_PATH.getParent());
        Files.writeString(USER_PATH, json);
    }
}
