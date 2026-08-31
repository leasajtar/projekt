package org.example.enteties.json;

import jakarta.json.bind.*;
import org.example.enteties.User;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class UserReader {
    public static void main(String[] args) {
        try {
            Jsonb jsonb = JsonbBuilder.create();

            String jsonLista = Files.readString(Paths.get("data/user.json"));

            List<User> users = jsonb.fromJson(
                    jsonLista,
                    new ArrayList<User>(){}.getClass().getGenericSuperclass()
            );

            System.out.println("Lista korisnika:");
            for (User u : users) {
                System.out.println(u.getUsername() + " – " + u.getEmail());
            }

        } catch (Exception e) {
            System.err.println("Greška: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public static List<User> readUsers() {
        try {
            System.out.println("Reading JSON from: " + Paths.get("data/user.json").toAbsolutePath());

            Jsonb jsonb = JsonbBuilder.create();
            String json = Files.readString(Paths.get("data/user.json"));

            return jsonb.fromJson(
                    json,
                    new ArrayList<User>(){}.getClass().getGenericSuperclass()
            );

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
