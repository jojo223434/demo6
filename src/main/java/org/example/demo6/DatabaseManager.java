package org.example.demo6;

import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:vindmoller.db";

    public static void initDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            String sql = """
                CREATE TABLE IF NOT EXISTS readings (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    timestamp TEXT,
                    wind_speed REAL,
                    wind_effect REAL,
                    wtg01 REAL,
                    wtg02 REAL,
                    wtg03 REAL,
                    wtg04 REAL,
                    wtg05 REAL,
                    wtg06 REAL
                );
            """;
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertReading(String timestamp, double windSpeed, double windEffect, double[] turbines) {
        String sql = """
            INSERT INTO readings (timestamp, wind_speed, wind_effect, wtg01, wtg02, wtg03, wtg04, wtg05, wtg06)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);
        """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, timestamp);
            pstmt.setDouble(2, windSpeed);
            pstmt.setDouble(3, windEffect);
            for (int i = 0; i < 6; i++) {
                pstmt.setDouble(4 + i, turbines[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
