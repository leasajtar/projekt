package org.example.utility;

import org.example.entities.*;
import org.example.entities.Record;
import org.example.repos.BookingRepos;
import org.example.repos.ItemRepos;
import org.example.repos.UserRepos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Drži dijeljeni popis nedavne aktivnosti koji jedna nit periodički
 * osvježava iz baze, dok druga (neovisna) nit isti trenutak podataka
 * sprema u backup.bin. Pristup dijeljenom popisu zaštićen je
 * {@link ReentrantLock}-om kako bi obje niti mogle sigurno raditi
 * nad istim resursom bez međusobnog ometanja.
 */
public final class HistoryMonitor {

    private static final Logger logger = LoggerFactory.getLogger(HistoryMonitor.class);
    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final ReentrantLock lock = new ReentrantLock();
    private static final List<String> recentActivity = new ArrayList<>();

    private static final BookingRepos bookingRepos = new BookingRepos();
    private static final UserRepos userRepos = new UserRepos();
    private static final ItemRepos itemRepos = new ItemRepos();

    private HistoryMonitor() {}

    /** Osvježava dijeljeni popis najnovijim rezervacijama iz baze. */
    public static void refreshFromDatabase() {
        List<Booking> latest = bookingRepos.findAll().stream().limit(10).toList();

        lock.lock();
        try {
            recentActivity.clear();
            Clock clock = Clock.systemDefaultZone();
            String now = LocalDateTime.now(clock).format(TS);
            for (Booking b : latest) {
                recentActivity.add("[" + now + "] " + b.getUser().getUsername() +
                        " -> " + b.getEventType().getEventType() + " (" + b.getDate() + ")");
            }
            logger.debug("Osvježen dijeljeni popis aktivnosti ({} zapisa)", recentActivity.size());
        } finally {
            lock.unlock();
        }
    }

    /** Sprema trenutno stanje baze u backup.bin. Koristi isti lock da se ne preklopi s osvježavanjem. */
    public static void saveSnapshot() {
        lock.lock();
        try {
            List<User> users = userRepos.findAll();
            List<Booking> bookings = bookingRepos.findAll();
            List<Item> items = itemRepos.findAll();
            Set<Record> records = new HashSet<>();

            UtilityBackUp.saveBackup(users, bookings, records, items);

            Clock clock = Clock.systemDefaultZone();
            recentActivity.add(0, "[" + LocalDateTime.now(clock).format(TS) + "] Snapshot saved to backup.bin");
            logger.info("Snimljen snapshot u backup.bin");
        } finally {
            lock.unlock();
        }
    }

    /** Vraća kopiju trenutnog popisa aktivnosti radi sigurnog prikaza u UI-ju. */
    public static List<String> snapshotOfActivity() {
        lock.lock();
        try {
            return new ArrayList<>(recentActivity);
        } finally {
            lock.unlock();
        }
    }

    /** Pokreće pozadinsku nit koja periodički zove {@link #refreshFromDatabase()}. */
    public static void startAutoRefresh(long everySeconds) {
        Thread refresher = new Thread(() -> {
            while (true) {
                try {
                    refreshFromDatabase();
                    Thread.sleep(everySeconds * 1000L);
                } catch (InterruptedException _) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "history-refresh");
        refresher.setDaemon(true);
        refresher.start();
    }
}