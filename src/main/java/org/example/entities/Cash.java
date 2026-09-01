package org.example.entities;


import java.math.BigDecimal;

/** Sučelje za rezervacije plaćene gotovinom, koje nose popust umjesto mogućnosti otkazivanja. */
public interface Cash {
    /** @return iznos cijene nakon primijenjenog popusta */
    BigDecimal popust();
}
