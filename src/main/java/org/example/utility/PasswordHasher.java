package org.example.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Hashira lozinke SHA-256 algoritmom (jednosmjerno) za spremanje u tekstualnu datoteku. */
public final class PasswordHasher {

    private PasswordHasher() {}

    public static String hash(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    public static boolean matches(String plainPassword, String hashedPassword) {
        return hash(plainPassword).equalsIgnoreCase(hashedPassword);
    }
}