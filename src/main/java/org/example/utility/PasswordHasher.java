package org.example.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/** Hashira lozinke SHA-256 algoritmom (jednosmjerno) za spremanje u tekstualnu datoteku. */
public final class PasswordHasher {

    private PasswordHasher() {}

    /**
     * @param plainPassword lozinka u čitljivom obliku
     * @return SHA-256 hash lozinke, zapisan kao heksadecimalni string
     */
    public static String hash(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    /**
     * @param plainPassword  lozinka u čitljivom obliku koju treba provjeriti
     * @param hashedPassword pohranjeni hash s kojim se uspoređuje
     * @return {@code true} ako hash unesene lozinke odgovara pohranjenom hashu
     */
    public static boolean matches(String plainPassword, String hashedPassword) {
        return hash(plainPassword).equalsIgnoreCase(hashedPassword);
    }
}