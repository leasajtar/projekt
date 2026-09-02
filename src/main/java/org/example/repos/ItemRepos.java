package org.example.repos;

import org.example.exceptions.DatabaseException;
import org.example.utility.DbUtil;
import org.example.entities.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repozitorij klasa za povezivanje baze podataka dogadanja.
 * */
public class ItemRepos {
    public static final Logger log = LoggerFactory.getLogger(ItemRepos.class);

    /**Provjerava postojanje tipa dogadanja u bazi podataka
     * @param eventType naziv tipa dogadanja
     * @return {@code true} ako stavka postoji, uprotivnom {@code false}
     * */
    public boolean eventExists(String eventType) {
        String sql = "SELECT 1 FROM items WHERE LOWER(event_type) = LOWER(?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, eventType);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check event type", e);
        }
    }

    /**Umetanje tipa dogadaja u bazu pdataka
     * @param item tip dogadaja
     * @return identifikacijski broj dogadanja u bazi
     * @throws DatabaseException u slucaju da se ne vrati generirani kljuc
     * */
    public long insert(Item item) {
        String sql = "INSERT INTO items(event_type, price) VALUES(?, ?)";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, item.getEventType());
            ps.setBigDecimal(2, item.getPrice());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    long id = keys.getLong(1);
                    item.setId((int) id);
                    return id;
                }
            }
            throw new DatabaseException("No generated key for items insert");

        } catch (SQLException e) {
            log.error("Failed to insert item", e);
            throw new RuntimeException("Failed to insert item", e);

        }
    }

    /**@return vraca sve stavke baze podataka*/
    public List<Item> findAll() {
        String sql = "SELECT id, event_type, price FROM items ORDER BY event_type";
        try (Connection c = DbUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Item> out = new ArrayList<>();
            while (rs.next()) {
                int id = (int) rs.getLong("id");
                String type = rs.getString("event_type");
                BigDecimal price = rs.getBigDecimal("price");

                Item it = new Item(id, type, price);
                out.add(it);
            }
            return out;

        } catch (SQLException e) {
            throw new RuntimeException("Failed to load items", e);
        }
    }
}