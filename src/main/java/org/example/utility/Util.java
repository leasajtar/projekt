package org.example.utility;

import org.example.entities.Booking;

import java.time.format.DateTimeFormatter;

/** Razne pomoćne (statičke) metode za validaciju unosa i filtriranje rezervacija. */
public final class Util {
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");


    private Util() {}

    /**
     * Provjerava jačinu lozinke: mora sadržavati barem jedno veliko slovo, malo
     * slovo, broj i poseban znak, te biti dugačka barem 8 znakova.
     *
     * @param password lozinka koju treba provjeriti
     * @return {@code true} ako lozinka zadovoljava sve uvjete
     */
    public static boolean passwordValidate(String password) {
        boolean hasLower = false;
        boolean hasUpper = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isLowerCase(c)) {
                hasLower = true;
            } else if (Character.isUpperCase(c)) {
                hasUpper = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }

        return hasLower && hasUpper && hasDigit && hasSpecial && (password.length() >= 8);
    }

    /**
     * Provjerava je li dani tekst valjana email adresa (osnovna provjera regularnim izrazom).
     *
     * @param email tekst koji treba provjeriti
     * @return {@code true} ako format odgovara email adresi
     */
    public static boolean emailValidate(String email) {
        if (email == null) return false;

        return email.matches(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        );
    }

    /**
     * Provjerava odgovara li rezervacija zadanom filtru (po korisničkom imenu,
     * događaju, datumu ili gradu), bez razlike velikih/malih slova.
     *
     * @param filter  vrsta filtera ("Username", "Event", "Date" ili "City")
     * @param value   vrijednost koju rezervacija mora sadržavati
     * @param booking rezervacija koju treba provjeriti
     * @return {@code true} ako rezervacija odgovara filtru (ili ako su ulazni podaci nepotpuni)
     */
    public static boolean filterSwitch(String filter, String value, Booking booking){
        if (booking == null || filter == null || value == null) {
            return true;
        }

        String v = value.toLowerCase();

        return switch (filter) {
            case "Username" -> hasText(booking.getUser() != null
                    ? booking.getUser().getUsername()
                    : null, v);

            case "Event" -> hasText(booking.getEventType() != null
                    ? booking.getEventType().getEventType()
                    : null, v);

            case "Date" -> booking.getDate() != null &&
                    booking.getDate().format(DATE_FMT).contains(value);

            case "City" -> hasText(booking.getLocation() != null
                    ? booking.getLocation().city()
                    : null, v);

            default -> true;
        };
    }

    /**
     * @param source izvorni tekst (može biti {@code null})
     * @param value  tekst koji se traži (mala slova)
     * @return {@code true} ako izvorni tekst nije {@code null} i sadrži traženu vrijednost
     */
    private static boolean hasText(String source, String value) {
        return source != null && source.toLowerCase().contains(value);
    }

}