package org.example.repos;

import org.example.utility.DbUtil;
import org.example.enteties.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepos {

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

    public long insert(User u) {
        String sql = "INSERT INTO users(username, password, email, phone) VALUES(?,?,?,?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());

            if (u.getEmail() == null || u.getEmail().isBlank()) {
                ps.setString(3, "");
            } else {
                ps.setString(3, u.getEmail());
            }

            if (u.getPhone() == null || u.getPhone().isBlank()) {
                ps.setString(4, "");
            } else {
                ps.setString(4, u.getPhone());
            }


            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    u.setId((int) id);
                    return id;
                }
            }
            throw new RuntimeException("No generated key returned for users insert");

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert user", e);
        }
    }

    public List<User> findAll() {
        String sql = "SELECT id, username, password, email, phone FROM users ORDER BY username";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<User> out = new ArrayList<>();
            while (rs.next()) {
                int id = (int) rs.getLong("id");

                User u = new User.UserBuilder(id,
                        rs.getString("username"),
                        rs.getString("password"))
                        .email(rs.getString("email"))
                        .phone(rs.getString("phone"))
                        .build();

                out.add(u);
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load users", e);
        }
    }
}
