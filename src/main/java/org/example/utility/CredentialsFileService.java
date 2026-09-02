package org.example.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

/**
 * Čita i piše podatke za prijavu (korisničko ime, hashirana lozinka, uloga)
 * iz tekstualne datoteke "credentials.txt", odvojeno od entiteta u bazi.
 * Format retka: username:hashedPassword:role
 */
public final class CredentialsFileService {

    private static final Logger logger = LoggerFactory.getLogger(CredentialsFileService.class);
    private static final Path FILE = Paths.get("credentials.txt");
    private static final String SEPARATOR = ":";

    private CredentialsFileService() {}

    /**
     * Stvara {@code credentials.txt} ako ne postoji i, ako u njoj još nema
     * admin računa, zasijava zadanog admina ({@code admin}/{@code admin123}).
     */
    public static synchronized void ensureDefaultAdmin() {
        try {
            if (Files.notExists(FILE)) {
                Files.createFile(FILE);
            }
            if (findLine("admin") == null) {
                appendLine("admin", PasswordHasher.hash("admin123"), "ADMIN");
                logger.info("Zasijan zadani admin račun u credentials.txt");
            }
        } catch (IOException e) {
            logger.error("Ne mogu inicijalizirati credentials.txt", e);
        }
    }

    /**
     * Dodaje vjerodajnice novog običnog korisnika u datoteku (hashira lozinku prije zapisa).
     *
     * @param username      korisničko ime
     * @param plainPassword lozinka u čitljivom (nehashiranom) obliku
     */
    public static synchronized void addUserCredentials(String username, String plainPassword) {
        try {
            appendLine(username, PasswordHasher.hash(plainPassword), "USER");
        } catch (IOException e) {
            logger.error("Ne mogu zapisati korisničke podatke za prijavu", e);
        }
    }

    /** Provjerava korisničko ime/lozinku/ulogu protiv tekstualne datoteke. */
    public static synchronized boolean validate(String username, String plainPassword, String role) {
        String line = findLine(username);
        if (line == null) return false;

        String[] parts = line.split(SEPARATOR, 3);
        if (parts.length != 3) return false;

        return parts[2].equalsIgnoreCase(role) && PasswordHasher.matches(plainPassword, parts[1]);
    }

    /**
     * Traži redak koji odgovara danom korisničkom imenu.
     *
     * @param username korisničko ime koje se traži
     * @return pronađeni redak ({@code username:hash:role}), ili {@code null} ako nije pronađen
     */
    private static String findLine(String username) {
        try {
            if (Files.notExists(FILE)) return null;
            List<String> lines = Files.readAllLines(FILE, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(SEPARATOR, 3);
                if (parts.length == 3 && parts[0].equalsIgnoreCase(username)) {
                    return line;
                }
            }
        } catch (IOException e) {
            logger.error("Greška pri čitanju credentials.txt", e);
        }
        return null;
    }

    /**
     * Dodaje jedan redak vjerodajnica na kraj datoteke.
     *
     * @param username       korisničko ime
     * @param hashedPassword već hashirana lozinka
     * @param role           uloga ("USER" ili "ADMIN")
     * @throws IOException ako zapisivanje u datoteku ne uspije
     */
    private static void appendLine(String username, String hashedPassword, String role) throws IOException {
        String line = username + SEPARATOR + hashedPassword + SEPARATOR + role;
        Files.writeString(FILE, line + System.lineSeparator(),
                StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}