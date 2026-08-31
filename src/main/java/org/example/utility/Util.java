package org.example.utility;

import org.example.entities.Booking;

import java.time.format.DateTimeFormatter;

public final class Util {
    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("dd.MM.yyyy");


    private Util() {}

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

    public static boolean emailValidate(String email) {
        if (email == null) return false;

        return email.matches(
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
        );
    }

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

    private static boolean hasText(String source, String value) {
        return source != null && source.toLowerCase().contains(value);
    }

}
