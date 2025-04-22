package com.pain001.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvLoader {
    private static final Logger LOGGER = Logger.getLogger(CsvLoader.class.getName());

    /**
     * Loads CSV data from a file.
     *
     * @param filePath Path to the CSV file.
     * @return List of maps where each map represents a row (column name → value).
     * @throws FileNotFoundException If the file does not exist.
     * @throws IOException If there is an issue reading the file.
     */
    public static List<Map<String, String>> loadCsvData1(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String headerLine = reader.readLine();
            if (headerLine == null || headerLine.isEmpty()) {
                throw new IOException("The CSV file '" + filePath + "' is empty.");
            }

            String[] headers = headerLine.split(",");
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                Map<String, String> row = new HashMap<>();

                for (int i = 0; i < headers.length; i++) {
                    row.put(headers[i].trim(), i < values.length ? values[i].trim() : "");
                }
                data.add(row);
            }

        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File '" + filePath + "' not found.", e);
            throw e;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while reading the file '" + filePath + "'.", e);
            throw e;
        }

        return data;
    }
    public static List<Map<String, String>> loadCsvData(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (CSVParser parser = CSVParser.parse(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                Map<String, String> row = new HashMap<>();
                for (String header : parser.getHeaderMap().keySet()) {
                    row.put(header, record.isMapped(header) ? record.get(header) : "");
                }
                data.add(row);
            }
        }

        catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File '" + filePath + "' not found.", e);
            throw e;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while reading the file '" + filePath + "'.", e);
            throw e;
        }

        return data;
    }

    public static void main(String[] args) {
        String filePath = "src/main/java/com/pain001/templates/pain00100103/template.csv"; // Example CSV file path

        try {
            List<Map<String, String>> csvData = loadCsvData(filePath);
            if (csvData.isEmpty()) {
                System.out.println("The CSV file is empty.");
            } else {
                System.out.println("CSV Data Loaded: " + csvData);
            }
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }
}
