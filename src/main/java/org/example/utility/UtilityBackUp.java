package org.example.utility;

import org.example.entities.*;
import org.example.entities.Record;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

/** Serijalizira/deserijalizira cjelokupno stanje aplikacije u/iz binarne datoteke {@code backup.bin}. */
public class UtilityBackUp {
    private UtilityBackUp(){}
    private static final Logger logger = LoggerFactory.getLogger(UtilityBackUp.class);

    /**
     * Slaže dane podatke u {@link BackupData} i serijalizira ih u {@code backup.bin}.
     *
     * @param users    korisnici koje treba spremiti
     * @param bookings rezervacije koje treba spremiti
     * @param records  zapisi (povijest) koje treba spremiti
     * @param items    stavke koje treba spremiti
     */
    public static void saveBackup(List<User> users,
                                  List<Booking> bookings,
                                  Set<Record> records,
                                  List<Item> items) {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("backup.bin"))) {
            BackupData backup = new BackupData(users, bookings, records, items);
            out.writeObject(backup);
            logger.info("Backup created successfully (backup.bin)");
        } catch (Exception e) {
            logger.error("Error saving backup", e);
        }
    }

    /**
     * Deserijalizira sadržaj {@code backup.bin}.
     *
     * @return učitani {@link BackupData}, ili {@code null} ako datoteka ne postoji ili dođe do greške
     */
    public static BackupData loadBackup() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("backup.bin"))) {
            BackupData data = (BackupData) in.readObject();
            logger.info("Backup loaded successfully");
            return data;
        } catch (Exception e) {
            logger.error("Error loading backup", e);
            return null;
        }
    }
}