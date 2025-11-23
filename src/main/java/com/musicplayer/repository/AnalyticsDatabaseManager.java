package com.musicplayer.repository;

import java.io.File;
import java.sql.*;

// DB Manager untuk analisis
public class AnalyticsDatabaseManager {
    private static AnalyticsDatabaseManager instance;
    private Connection connection;

    // Simpan di folder data/
    private static final String DB_FOLDER = "data";
    private static final String DB_FILE = "analytics.db";
    private static final String DB_PATH = DB_FOLDER + "/" + DB_FILE;

    private AnalyticsDatabaseManager() {
        initializeDatabase();
    }

    public static synchronized AnalyticsDatabaseManager getInstance() {
        if (instance == null) {
            instance = new AnalyticsDatabaseManager();
        }
        return instance;
    }

    private void initializeDatabase() {
        try {
            // Buat folder data/ jika belum ada
            createDataFolderIfNotExists();

            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");

            // Create connection
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);

            // Enable foreign keys
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON");
            }

            System.out.println(" Analytics SQLite database connected: " + DB_PATH);

            // Create tables
            createTables();

        } catch (ClassNotFoundException e) {
            System.err.println(" SQLite JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println(" Failed to initialize analytics database");
            e.printStackTrace();
        }
    }

    // Buat folder data/ jika belum ada
    private void createDataFolderIfNotExists() {
        File dataFolder = new File(DB_FOLDER);
        if (!dataFolder.exists()) {
            if (dataFolder.mkdirs()) {
                System.out.println(" Created data folder: " + dataFolder.getAbsolutePath());
            } else {
                System.err.println(" Failed to create data folder");
            }
        }
    }

    private void createTables() {
        String createPlayHistoryTable = """
                    CREATE TABLE IF NOT EXISTS play_history (
                        id INTEGER PRIMARY KEY AUTOINCREMENT,
                        track_id TEXT NOT NULL,
                        track_title TEXT NOT NULL,
                        artist TEXT,
                        genre TEXT,
                        duration INTEGER DEFAULT 0,
                        played_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                    )
                """;

        // Create index untuk performance
        String createIndexTrackId = """
                    CREATE INDEX IF NOT EXISTS idx_play_history_track_id
                    ON play_history(track_id)
                """;

        String createIndexPlayedAt = """
                    CREATE INDEX IF NOT EXISTS idx_play_history_played_at
                    ON play_history(played_at)
                """;

        String createIndexArtist = """
                    CREATE INDEX IF NOT EXISTS idx_play_history_artist
                    ON play_history(artist)
                """;

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createPlayHistoryTable);
            stmt.execute(createIndexTrackId);
            stmt.execute(createIndexPlayedAt);
            stmt.execute(createIndexArtist);
            System.out.println(" Analytics table 'play_history' ready");
        } catch (SQLException e) {
            System.err.println(" Error creating analytics tables");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Analytics database connection closed");
            }
        } catch (SQLException e) {
            System.err.println(" Error closing analytics database");
            e.printStackTrace();
        }
    }
}