package org.example.entities;

import jakarta.json.bind.annotation.JsonbProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Vrsta dogadaja koji se moze rezervirati i njegova cijena.
 * */
public class Item implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Item.class);
    private int id;
    @JsonbProperty("eventType")
    private String eventType;
    @JsonbProperty("price")
    private BigDecimal price;

    /** Prazan konstruktor, potreban za JSON-B/JDBC mapiranje. */
    public Item() {}

    /**
     * Stvara novu stavku (vrstu događaja) i bilježi je u log; upozorava ako je cijena negativna.
     *
     * @param id        identifikator stavke
     * @param eventType naziv/vrsta događaja
     * @param price     cijena
     */
    public Item(int id, String eventType, BigDecimal price) {
        this.id = id;
        this.eventType = eventType;
        this.price = price;
        logger.info("Item '{}' dodan", eventType);
        logger.debug("Cijena itema: {}", price);
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            logger.warn("Negativna cijena za item '{}': {}", eventType, price);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return getEventType();
    }
}