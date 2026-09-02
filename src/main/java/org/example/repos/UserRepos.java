package org.example.repos;

import org.example.entities.User;
import org.example.exceptions.DatabaseException;
import org.example.utility.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *  Repozitorij klasa za povezivanje baze podataka korisnika.
 *  */
public class UserRepos {

    /** Provjerava postoji li korisnik u bazi podataka
     * @param username ime korisnika
     * @return {@code true} ako postoji korisnik s istim imenom
     * */
    public boolean usernameExists(String username) {
        String sql = "SELECT 1 FROM users WHERE LOWER(username) = LOWER(?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check username", e);
        }
    }

    /**Unos korisnika u bazu podataka.
     * @param u objekt korisnika
     * @return identifikacijski broj korisnika u bazi
     * @throws DatabaseException baca iznimku ukoliko ne uspije upisati korisnika u bazu podataka
     * */
    public long insert(User u) throws DatabaseException {
        String sql = "INSERT INTO users(username, password, email, phone, role) VALUES(?,?,?,?,'USER')";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getEmail() == null || u.getEmail().isBlank() ? "" : u.getEmail());
            ps.setString(4, u.getPhone() == null || u.getPhone().isBlank() ? "" : u.getPhone());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    u.setId((int) id);
                    return id;
                }
            }
            throw new DatabaseException("No generated key returned for users insert");

        } catch (SQLException e) {
            throw new DatabaseException("Failed to insert user", e);
        }
    }

    /** Vraća samo obične korisnike, koristi se za dropdown pri rezervaciji. */
    public List<User> findAll() {
        String sql = "SELECT id, username, password, email, phone FROM users WHERE role = 'USER' ORDER BY username";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<User> out = new ArrayList<>();
            while (rs.next()) {
                out.add(mapUser(rs));
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }

    /**
     * Dohvaća korisnika po korisničkom imenu — koristi se nakon što
     * {@link org.example.utility.CredentialsFileService} potvrdi prijavu,
     * kako bi se dobili id/email/telefon potrebni za rezervacije.
     */
    public User findByUsername(String username) {
        String sql = "SELECT id, username, password, email, phone FROM users WHERE LOWER(username) = LOWER(?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapUser(rs) : null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find user by username", e);
        }
    }

    /** Mapira jedan redak rezultata upita u {@link User objekt}
     * @param rs redak rezultaka upita
     * @return mapirani korisnik
     * @throws SQLException ako citanje stupaca ne uspije
     * */
    private User mapUser(ResultSet rs) throws SQLException {
        return new User.UserBuilder(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .build();
    }
}