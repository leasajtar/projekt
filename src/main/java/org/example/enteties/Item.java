package org.example.enteties;

import jakarta.json.bind.annotation.JsonbProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.math.BigDecimal;


public class Item implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Item.class);
    private int id;
    @JsonbProperty("eventType")
    private String eventType;
    @JsonbProperty("price")
    private BigDecimal price;

    public Item() {}
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