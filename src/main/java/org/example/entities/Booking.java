package org.example.entities;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.json.bind.annotation.JsonbTransient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class Booking implements Serializable {
    @JsonbTransient
    public static final Logger logger = LoggerFactory.getLogger(Booking.class);

    private long id;

    protected User user;

    @JsonbDateFormat("yyyy-MM-dd")
    public LocalDate date;

    @JsonbDateFormat("HH:mm:ss")
    public LocalTime time;

    protected Item eventType;
    protected Location location;

    protected String band;

    public Booking(){}

    public Booking(User user, LocalDate date, LocalTime time, Item eventType, Location location, String band) {
        this.user = user;
        this.date = date;
        this.time = time;
        this.eventType = eventType;
        this.location = location;
        this.band = band;

        if (logger != null && user != null && eventType != null) {
            logger.info("Nova rezervacija kreirana za korisnika: {}", user.getUsername());
            logger.debug("Detalji rezervacije -> Event: {}, Datum: {}, Vrijeme: {}",
                    eventType.getEventType(), date, time);
        }
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Item getEventType() { return eventType; }
    public String getBand() { return band; }
    public void setBand(String band) { this.band = band; }
    public void info(){/*overridden*/}
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Location getLocation() { return location; }
    public void setLocation(Location location) { this.location = location; }
    public void setEventType(Item eventType) { this.eventType = eventType; }
    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}