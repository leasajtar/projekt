package org.example.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class BookingBackupService {

    private static final Logger logger = LoggerFactory.getLogger(BookingBackupService.class);

    private static final String TABLE = "bookings";
    private static final String BACKUP = "bookings_BACKUP";

    private static ScheduledExecutorService scheduler;
    private static final AtomicBoolean running = new AtomicBoolean(false);

    private BookingBackupService() {}

    public static void backupNowAsync() {
        Thread.ofVirtual().start(() -> {
            if (!running.compareAndSet(false, true)) return;

            try {
                backupOnce();
                logger.info("Backup completed: {}", BACKUP);
            } catch (Exception e) {
                logger.error("Backup failed", e);
            } finally {
                running.set(false);
            }
        });
    }

    public static void startAutoBackup(long seconds) {
        if (scheduler != null && !scheduler.isShutdown()) return;

        scheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "booking-backup-scheduler");
            t.setDaemon(true);
            return t;
        });

        scheduler.scheduleAtFixedRate(
                BookingBackupService::backupNowAsync,
                0,
                seconds,
                TimeUnit.SECONDS
        );
    }

    public static void stopAutoBackup() {
        if (scheduler != null) {
            scheduler.shutdownNow();
            scheduler = null;
        }
    }

    private static void backupOnce() throws SQLException {
        try (Connection c = DbUtil.getConnection();
             Statement st = c.createStatement()) {

            c.setAutoCommit(false);

            st.executeUpdate("DROP TABLE IF EXISTS " + BACKUP);
            st.executeUpdate("CREATE TABLE " + BACKUP + " AS SELECT * FROM " + TABLE + " WHERE 1=0");
            st.executeUpdate("INSERT INTO " + BACKUP + " SELECT * FROM " + TABLE);

            c.commit();
        }
    }
}