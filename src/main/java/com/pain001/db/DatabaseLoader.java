package com.pain001.db;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DatabaseLoader {
    private static final Logger LOGGER = Logger.getLogger(DatabaseLoader.class.getName());
    private static final String SQLITE_JDBC_URL_PREFIX = "jdbc:sqlite:";

    public static List<DataRecord> loadDbData(String dataFilePath, String tableName) throws Exception {
        // Validate if the SQLite file exists
        File file = new File(dataFilePath);
        if (!file.exists()) {
            throw new Exception("SQLite file '" + dataFilePath + "' does not exist.");
        }

        // Sanitize table name
        tableName = sanitizeTableName(tableName);
        List<DataRecord> data = new ArrayList<>();

        // Establish SQLite connection
        try (Connection conn = DriverManager.getConnection(SQLITE_JDBC_URL_PREFIX + dataFilePath);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName);
             ResultSet rs = stmt.executeQuery()) {

            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                DataRecord record = new DataRecord();
                for (int i = 1; i <= columnCount; i++) {
                    record.addField(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                data.add(record);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error accessing SQLite database: " + e.getMessage());
            throw e;
        }
        return data;
    }

    private static String sanitizeTableName(String tableName) {
        return tableName.replaceAll("[^a-zA-Z0-9_]", "_");
    }
}
