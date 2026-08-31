package org.example.utility;

import org.example.enteties.*;
import org.example.enteties.Record;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.Set;

public class UtilityBackUp {
    public static void saveBackup(List<User> users,
                                  List<Booking> bookings,
                                  Set<Record> records,
                                  List<Item> items) {

        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("backup.bin"))) {
            BackupData backup = new BackupData(users, bookings, records, items);
            out.writeObject(backup);
            System.out.println("Backup created successfully (backup.bin).");
        } catch (Exception e) {
            System.out.println("Error saving backup: " + e.getMessage());
        }
    }

    public static BackupData loadBackup() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("backup.bin"))) {
            BackupData data = (BackupData) in.readObject();
            System.out.println("Backup loaded successfully.");
            return data;
        } catch (Exception e) {
            System.out.println("Error loading backup: " + e.getMessage());
            return null;
        }
    }
}
