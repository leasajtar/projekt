package org.example.repos;

import org.example.utility.DbUtil;
import org.example.enteties.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class BookingRepos {

    public void insert(Booking b) {
        String sql = """
            INSERT INTO bookings(user_id, item_id, date, time, street, house, city)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, b.getUser().getId());
            ps.setLong(2, b.getEventType().getId());
            ps.setDate(3, Date.valueOf(b.getDate()));
            ps.setTime(4, Time.valueOf(b.getTime()));

            String full = b.getLocation().adress();
            ps.setString(5, full);
            ps.setString(6, "");
            ps.setString(7, b.getLocation().city());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert booking", e);
        }
    }

    public List<Booking> findAll(UserRepos ur, ItemRepos ir) {
        String sql = """
            SELECT b.id, b.date, b.time, b.street, b.house, b.city,
                   u.id AS u_id, u.username,
                   i.id AS i_id, i.event_type, i.price
            FROM bookings b
            JOIN users u ON u.id = b.user_id
            JOIN items i ON i.id = b.item_id
            ORDER BY b.date DESC, b.time DESC
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Booking> out = new ArrayList<>();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("u_id"));
                u.setUsername(rs.getString("username"));

                Item it = new Item();
                it.setId(rs.getInt("i_id"));
                it.setEventType(rs.getString("event_type"));
                it.setPrice(rs.getBigDecimal("price"));

                LocalDate d = rs.getDate("date").toLocalDate();
                LocalTime t = rs.getTime("time").toLocalTime();

                Location loc = new Location(
                        (rs.getString("street") + " " + rs.getString("house")).trim(),
                        rs.getString("city")
                );

                Booking b = new Booking(u, d, t, it, loc, null);
                out.add(b);
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load bookings", e);
        }
    }

    public List<Booking> findAll() {
        String sql = """
            SELECT
                b.id,
                b.date,
                b.time,
                b.street,
                b.house,
                b.city,
                u.id   AS user_id,
                u.username,
                u.password,
                u.email,
                u.phone,
                i.id   AS item_id,
                i.event_type,
                i.price
            FROM bookings b
            JOIN users u ON u.id = b.user_id
            JOIN items i ON i.id = b.item_id
            ORDER BY b.date DESC, b.time DESC
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Booking> out = new ArrayList<>();

            while (rs.next()) {
                // User
                int uid = (int) rs.getLong("user_id");
                User user = new User.UserBuilder(uid,
                        rs.getString("username"),
                        rs.getString("password"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .build();

                // Item
                int iid = (int) rs.getLong("item_id");
                Item item = new Item(iid,
                        rs.getString("event_type"),
                        rs.getBigDecimal("price"));

                // Date/time
                LocalDate date = rs.getDate("date").toLocalDate();
                LocalTime time = rs.getTime("time").toLocalTime();

                // Location (you store street+house+city in DB)
                String street = rs.getString("street");
                String house = rs.getString("house");
                String city = rs.getString("city");

                Location loc = new Location((street + " " + house).trim(), city);

                // Booking (adapt if your constructor differs)
                Booking booking = new Booking(user, date, time, item, loc, null);

                out.add(booking);
            }

            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load bookings from DB", e);
        }
    }

    public Booking findLatest() {
        String sql = """
            SELECT b.id,
                   b.date, b.time,
                   b.street, b.city,
                   u.id AS u_id, u.username, u.email, u.password, u.phone,
                   i.id AS i_id, i.event_type, i.price
            FROM bookings b
            JOIN users u ON u.id = b.user_id
            JOIN items i ON i.id = b.item_id
            ORDER BY b.id DESC
            LIMIT 1
        """;

        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (!rs.next()) return null;

            // USER
            User user = new User.UserBuilder(
                    rs.getInt("u_id"),
                    rs.getString("username"),
                    rs.getString("password")
            )
                    .email(rs.getString("email"))
                    .phone(rs.getString("phone"))
                    .build();

            // ITEM
            Item item = new Item(
                    rs.getInt("i_id"),
                    rs.getString("event_type"),
                    rs.getBigDecimal("price")
            );

            // LOCATION (street + city)
            Location location = new Location(
                    rs.getString("street"),
                    rs.getString("city")
            );

            // BOOKING
            Booking b = new Booking();
            b.setUser(user);
            b.setEventType(item);
            b.setLocation(location);
            b.setDate(rs.getDate("date").toLocalDate());
            b.setTime(rs.getTime("time").toLocalTime());

            return b;

        } catch (Exception e) {
            throw new RuntimeException("Failed to load latest booking", e);
        }
    }
}