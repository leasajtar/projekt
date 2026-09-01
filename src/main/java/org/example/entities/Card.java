package org.example.entities;

/**
 * Sucelje za rezervaciju koje placaju karticom i mogu otkazati.
 * Jedina dopustena implementacija je {@link Cancellable} zbog kljucne rijeci sealed.
 * */
public sealed interface Card permits Cancellable {
    void cancel();
}
