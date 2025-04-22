package com.pain001.csv;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CsvValidator {
    private static final Logger LOGGER = Logger.getLogger(CsvValidator.class.getName());

    // Define required columns with expected data types
    private static final Map<String, Class<?>> REQUIRED_COLUMNS = new HashMap<>();
    static {
        REQUIRED_COLUMNS.put("id", Integer.class);
        REQUIRED_COLUMNS.put("date", LocalDate.class);
        REQUIRED_COLUMNS.put("nb_of_txs", Integer.class);
        REQUIRED_COLUMNS.put("ctrl_sum", Double.class);
        REQUIRED_COLUMNS.put("initiator_name", String.class);
        REQUIRED_COLUMNS.put("payment_information_id", String.class);
        REQUIRED_COLUMNS.put("payment_method", String.class);
        REQUIRED_COLUMNS.put("batch_booking", Boolean.class);
        REQUIRED_COLUMNS.put("service_level_code", String.class);
        REQUIRED_COLUMNS.put("requested_execution_date", LocalDate.class);
        REQUIRED_COLUMNS.put("debtor_name", String.class);
        REQUIRED_COLUMNS.put("debtor_account_IBAN", String.class);
        REQUIRED_COLUMNS.put("debtor_agent_BIC", String.class);
        REQUIRED_COLUMNS.put("forwarding_agent_BIC", String.class);
        REQUIRED_COLUMNS.put("charge_bearer", String.class);
        REQUIRED_COLUMNS.put("payment_id", String.class);
        REQUIRED_COLUMNS.put("payment_amount", Double.class);
        REQUIRED_COLUMNS.put("currency", String.class);
        REQUIRED_COLUMNS.put("creditor_agent_BIC", String.class);
        REQUIRED_COLUMNS.put("creditor_name", String.class);
        REQUIRED_COLUMNS.put("creditor_account_IBAN", String.class);
        REQUIRED_COLUMNS.put("remittance_information", String.class);
        // Make the map unmodifiable if needed
        // REQUIRED_COLUMNS = Collections.unmodifiableMap(REQUIRED_COLUMNS);
    }

    /**
     * Validates the CSV data before processing.
     *
     * @param data List of rows where each row is a map of column names to values.
     * @return true if the data is valid, false otherwise.
     */
    public static boolean validateCsvData(List<Map<String, String>> data) {
        if (data.isEmpty()) {
            LOGGER.severe("Error: The CSV data is empty.");
            return false;
        }

        boolean isValid = true;

        for (Map<String, String> row : data) {
            List<String> missingColumns = new ArrayList<>();
            List<String> invalidColumns = new ArrayList<>();

            for (Map.Entry<String, Class<?>> entry : REQUIRED_COLUMNS.entrySet()) {
                String column = entry.getKey();
                Class<?> expectedType = entry.getValue();
                String value = row.get(column);

                if (value == null || value.trim().isEmpty()) {
                    missingColumns.add(column);
                    isValid = false;
                } else {
                    try {
                        validateDataType(value, expectedType);
                    } catch (Exception e) {
                        invalidColumns.add(column);
                        isValid = false;
                    }
                }
            }

            if (!missingColumns.isEmpty()) {
                LOGGER.severe("Error: Missing values for columns " + missingColumns + " in row: " + row);
            }
            if (!invalidColumns.isEmpty()) {
                LOGGER.severe("Error: Invalid data types for columns " + invalidColumns + " in row: " + row);
            }
        }

        return isValid;
    }

    /**
     * Validates if the value matches the expected data type.
     *
     * @param value        The value to validate.
     * @param expectedType The expected Java data type.
     * @throws Exception If the value does not match the expected type.
     */
    private static void validateDataType(String value, Class<?> expectedType) throws Exception {
        if (expectedType == Integer.class) {
            Integer.parseInt(value);
        } else if (expectedType == Double.class) {
            Double.parseDouble(value);
        } else if (expectedType == Boolean.class) {
            if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                throw new IllegalArgumentException("Invalid boolean value: " + value);
            }
        } else if (expectedType == LocalDate.class) {
            parseDate(value);
        } else if (expectedType == String.class) {
            // Strings are always valid, so no need to check.
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + expectedType.getName());
        }
    }

    /**
     * Parses a date string and ensures it matches the expected format.
     *
     * @param dateStr The date string.
     * @throws Exception If the format is incorrect.
     */
    private static void parseDate(String dateStr) throws Exception {
        if (dateStr.endsWith("Z")) {
            dateStr = dateStr.replace("Z", "+00:00");
        }
        try {
            DateTimeFormatter.ISO_LOCAL_DATE.parse(dateStr);
        } catch (Exception e) {
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").parse(dateStr);
        }
    }

    public static void main(String[] args) {
        // Example CSV Data
        List<Map<String, String>> csvData = new ArrayList<>();
        Map<String, String> row1 = new HashMap<>();
        row1.put("id", "1");
        row1.put("date", "2024-02-27");
        row1.put("nb_of_txs", "5");
        row1.put("ctrl_sum", "1500.75");
        row1.put("initiator_name", "John Doe");
        row1.put("payment_information_id", "PAY123");
        row1.put("payment_method", "Credit Transfer");
        row1.put("batch_booking", "true");
        row1.put("service_level_code", "SEPA");
        row1.put("requested_execution_date", "2024-03-01");
        row1.put("debtor_name", "Alice");
        row1.put("debtor_account_IBAN", "DE89370400440532013000");
        row1.put("debtor_agent_BIC", "DEUTDEFF");
        row1.put("forwarding_agent_BIC", "BOFAUS3N");
        row1.put("charge_bearer", "SHA");
        row1.put("payment_id", "PID456");
        row1.put("payment_amount", "500.50");
        row1.put("currency", "EUR");
        row1.put("creditor_agent_BIC", "CITIFRPP");
        row1.put("creditor_name", "Bob");
        row1.put("creditor_account_IBAN", "FR1420041010050500013M02606");
        row1.put("remittance_information", "Invoice 789");

        csvData.add(row1);

        boolean result = validateCsvData(csvData);
        System.out.println("CSV validation result: " + result);
    }
}
