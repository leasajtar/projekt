package org.example.repos;

import org.example.exceptions.DatabaseException;
import org.example.utility.DbUtil;
import org.example.entities.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Repozitorij klasa za povezivanje baze podataka rezervacija.
 * */
public class BookingRepos {

    private static final String SELECT_BASE = """
            SELECT
                b.id, b.date, b.time, b.street, b.house, b.city,
                u.id AS user_id, u.username, u.password, u.email, u.phone,
                i.id AS item_id, i.event_type, i.price
            FROM bookings b
            JOIN users u ON u.id = b.user_id
            JOIN items i ON i.id = b.item_id
            """;

    /**Umetanje nove rezervacije u bazu
     *
     * @param b rezervacija
     * @throws DatabaseException u slucaju greske sa bazom podataka
     * */
    public void insert(Booking b) throws DatabaseException {
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
            ps.setString(5, b.getLocation().adress());
            ps.setString(6, "");
            ps.setString(7, b.getLocation().city());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert booking", e);
        }
    }

    /**@return  sve rezervacije u bazi podataka od najnovije do najstarije*/
    public List<Booking> findAll() {
        return query(SELECT_BASE + " ORDER BY b.date DESC, b.time DESC", ps -> {});
    }

    /**@param  userId identifikacijski broj korisnika
     * @return sve rezervacije korisnika od najnovije do najstarije*/
    public List<Booking> findByUser(int userId) {
        return query(SELECT_BASE + " WHERE u.id = ? ORDER BY b.date DESC, b.time DESC",
                ps -> ps.setInt(1, userId));
    }

    /** @return najnovija rezervacija*/
    public Booking findLatest() {
        List<Booking> result = query(SELECT_BASE + " ORDER BY b.id DESC LIMIT 1", ps -> {});
        return result.isEmpty() ? null : result.get(0);
    }

    /**Brise rezervaciju pod unesenim identifikacijskom broju
     * @param bookingId identifikacijski broj rezervacije */
    public void delete(long bookingId) {
        String sql = "DELETE FROM bookings WHERE id = ?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to delete booking", e);
        }
    }


    /** Azurira podatke rezervacije u bazi podataka
     * @param bookingId     identifikacijski broj rezervacije
     * @param date          datum rezervacije
     * @param location      lokacija rezervacije
     * @param time          vrijeme rezervacije
     * */
    public void update(long bookingId, java.time.LocalDate date, java.time.LocalTime time, Location location) {
        String sql = "UPDATE bookings SET date = ?, time = ?, street = ?, city = ? WHERE id = ?";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            ps.setTime(2, Time.valueOf(time));
            ps.setString(3, location.adress());
            ps.setString(4, location.city());
            ps.setLong(5, bookingId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update booking", e);
        }
    }

    /** Funkcionalno sučelje za povezivanje parametara na {@link PreparedStatement} prije izvršavanja upita. */
    @FunctionalInterface
    private interface Binder {
        /**
         * Postavlja parametre pripremljene naredbe.
         * @param ps pripremljena naredba na koju treba povezati parametre
         * @throws SQLException ako povezivanje parametara ne uspije
         */
        void bind(PreparedStatement ps) throws SQLException;
    }

    /** Izvrsava SQL upit i mapira rezultat u listu.
     *
     * @param sql SQL upit
     * @param binder povezuje dodatne parametre na upit
     * @return rezultat upita, mapiran u listu
     * */
    private List<Booking> query(String sql, Binder binder) {
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            binder.bind(ps);

            try (ResultSet rs = ps.executeQuery()) {
                List<Booking> out = new ArrayList<>();
                while (rs.next()) out.add(mapRow(rs));
                return out;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to load bookings", e);
        }
    }

    /**
     * Mapira jedan redak rezultata upita u {@link Booking} objekt.
     *
     * @param rs redak rezultata upita
     * @return mapirana rezervacija
     * @throws SQLException ako čitanje stupaca ne uspije
     */
    private Booking mapRow(ResultSet rs) throws SQLException {
        User user = new User.UserBuilder(
                rs.getInt("user_id"), rs.getString("username"), rs.getString("password"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .build();

        Item item = new Item(rs.getInt("item_id"), rs.getString("event_type"), rs.getBigDecimal("price"));

        Location location = new Location(
                (rs.getString("street") + " " + rs.getString("house")).trim(),
                rs.getString("city"));

        Booking booking = new Booking();
        booking.setId(rs.getLong("id"));
        booking.setUser(user);
        booking.setEventType(item);
        booking.setLocation(location);
        booking.setDate(rs.getDate("date").toLocalDate());
        booking.setTime(rs.getTime("time").toLocalTime());
        return booking;
    }

    /**
     * Ažurira postojeću rezervaciju.
     * @param b rezervacija s ažuriranim podacima
     */
    public void update(Booking b) {
        String sql = """
            UPDATE bookings
            SET user_id = ?, item_id = ?, date = ?, time = ?, street = ?, house = ?, city = ?
            WHERE id = ?
        """;
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, b.getUser().getId());
            ps.setLong(2, b.getEventType().getId());
            ps.setDate(3, Date.valueOf(b.getDate()));
            ps.setTime(4, Time.valueOf(b.getTime()));
            ps.setString(5, b.getLocation().adress());
            ps.setString(6, "");
            ps.setString(7, b.getLocation().city());
            ps.setLong(8, b.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update booking", e);
        }
    }
}