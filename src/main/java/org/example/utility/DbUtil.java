package org.example.utility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Središnja klasa za pristup bazi podataka: čita postavke veze iz
 * {@code database.properties}, otvara konekcije, i inicijalizira
 * potrebne tablice ({@code users}, {@code items}, {@code bookings}) pri
 * prvom pokretanju aplikacije.
 */
public final class DbUtil {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input =
                     DbUtil.class.getResourceAsStream("/database.properties")) {

            if (input == null) {
                throw new IllegalStateException(
                        "database.properties not found"
                );
            }

            properties.load(input);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "Could not load database.properties",
                    e
            );
        }
    }

    private DbUtil() {}

    /**
     * Otvara novu konekciju prema bazi, koristeći URL/korisnika/lozinku
     * učitane iz {@code database.properties}. Pozivatelj je odgovoran
     * za zatvaranje konekcije (preporučeno kroz try-with-resources).
     *
     * @return nova otvorena konekcija
     * @throws SQLException          ako otvaranje konekcije ne uspije
     * @throws IllegalStateException ako {@code db.url} nedostaje u postavkama
     */
    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.user");
        String password = properties.getProperty("db.password");

        if (url == null) {
            throw new IllegalStateException(
                    "Property db.url is missing from database.properties"
            );
        }

        return DriverManager.getConnection(
                url,
                user,
                password
        );
    }

    /**
     * Stvara tablice {@code users}, {@code items} i {@code bookings} ako još
     * ne postoje, dodaje stupac {@code role} ako nedostaje (za baze stvorene
     * prije njegovog uvođenja), i zasijava zadani admin račun.
     *
     * @throws RuntimeException ako inicijalizacija baze ne uspije
     */
    public static void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {

            s.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id IDENTITY PRIMARY KEY,
                    username VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NULL,
                    phone VARCHAR(100) NULL,
                    role VARCHAR(10) NOT NULL DEFAULT 'USER'
                );
            """);

            s.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS role VARCHAR(10) NOT NULL DEFAULT 'USER';");

            s.execute("""
                CREATE TABLE IF NOT EXISTS items (
                    id IDENTITY PRIMARY KEY,
                    event_type VARCHAR(150) NOT NULL,
                    price DECIMAL(12,2) NOT NULL
                );
            """);

            s.execute("""
                CREATE TABLE IF NOT EXISTS bookings (
                    id IDENTITY PRIMARY KEY,
                    user_id BIGINT NOT NULL,
                    item_id BIGINT NOT NULL,
                    date DATE NOT NULL,
                    time TIME NOT NULL,
                    street VARCHAR(200) NOT NULL,
                    house VARCHAR(50) NOT NULL,
                    city VARCHAR(80) NOT NULL,
                    FOREIGN KEY (user_id) REFERENCES users(id),
                    FOREIGN KEY (item_id) REFERENCES items(id)
                );
            """);

            s.execute("""
                MERGE INTO users (username, password, email, phone, role)
                KEY(username)
                VALUES ('admin', 'admin123', '', '', 'ADMIN');
            """);

        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }
}