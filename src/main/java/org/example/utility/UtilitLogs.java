package org.example.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class UtilitLogs {
    private static final Logger logger = LoggerFactory.getLogger(UtilitLogs.class);

    public static final String LOG_FILE = "actions.xml";

    public static void logAction(String action) {
        try {
            File file = new File(LOG_FILE);
            boolean exists = file.exists();

            FileWriter fw = new FileWriter(file, true);

            if (!exists) {
                fw.write("<log>\n");
            }

            fw.write("  <action>" + action + "</action>\n");

            fw.close();
        } catch (Exception e) {
            logger.error("Greška pri pisanju u XML log", e);
        }
    }

    public static void closeXmlLog() {
        try {
            File file = new File(LOG_FILE);
            if (!file.exists()) return;

            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            if (!content.trim().endsWith("</log>")) {
                FileWriter fw = new FileWriter(file, true);
                fw.write("</log>\n");
                fw.close();
            }
        } catch (Exception ignored) {}
    }

    // Namjerni, korisnički izlaz (Lab 6 zahtjev: ispis loga bez tagova) — ostaje System.out, nije debug.
    public static void printLogWithoutTags() {
        try {
            File file = new File(LOG_FILE);
            if (!file.exists()) {
                System.out.println("Log je prazan.");
                return;
            }

            List<String> lines = java.nio.file.Files.readAllLines(file.toPath());

            System.out.println("----- LOG AKCIJA -----");
            for (String line : lines) {
                String clean = line.replace("<log>", "")
                        .replace("</log>", "")
                        .replace("<action>", "")
                        .replace("</action>", "")
                        .trim();
                if (!clean.isEmpty()) System.out.println(clean);
            }
            System.out.println("----------------------");

        } catch (Exception e) {
            logger.error("Greška pri čitanju XML loga", e);
        }
    }
}