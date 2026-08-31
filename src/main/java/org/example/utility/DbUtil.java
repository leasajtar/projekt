package org.example.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public final class DbUtil {

    private static final String URL = "jdbc:h2:tcp://localhost/~/test";
    private static final String USER = "sa";
    private static final String PASS = "sa";

    private DbUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void init() {
        try (Connection c = getConnection(); Statement s = c.createStatement()) {

            s.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id IDENTITY PRIMARY KEY,
                    username VARCHAR(100) NOT NULL UNIQUE,
                    password VARCHAR(100) NOT NULL,
                    email VARCHAR(100) NULL,
                    phone VARCHAR(100) NULL
                );
            """);

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

        } catch (SQLException e) {
            throw new RuntimeException("DB init failed", e);
        }
    }
}
