package org.example.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

public class UtilitLogs {
    private UtilitLogs() {}
    private static final Logger logger = LoggerFactory.getLogger(UtilitLogs.class);

    public static final String LOG_FILE = "actions.xml";

    public static void logAction(String action) {
        File file = new File(LOG_FILE);
        try(FileWriter fw = new FileWriter(file, true)) {

            boolean exists = file.exists();



            if (!exists) {
                fw.write("<log>\n");
            }

            fw.write("  <action>" + action + "</action>\n");
        } catch (Exception e) {
            logger.error("Greška pri pisanju u XML log", e);
        }
    }

    public static void closeXmlLog() {
        File file = new File(LOG_FILE);
        try(FileWriter fw = new FileWriter(file, true)) {
            if (!file.exists()) return;

            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            if (!content.trim().endsWith("</log>")) {
                fw.write("</log>\n");
            }
        } catch (Exception ignored) {
            logger.trace("ignored exception", ignored);
        }
    }

    public static void printLogWithoutTags() {
        try {
            File file = new File(LOG_FILE);
            if (!file.exists()) {
                logger.warn("Log je prazan.");
                return;
            }

            List<String> lines = java.nio.file.Files.readAllLines(file.toPath());

            for (String line : lines) {
                String clean = line.replace("<log>", "")
                        .replace("</log>", "")
                        .replace("<action>", "")
                        .replace("</action>", "")
                        .trim();
                if (!clean.isEmpty()) logger.info(clean);
            }

        } catch (Exception e) {
            logger.error("Greška pri čitanju XML loga", e);
        }
    }
}