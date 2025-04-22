package com.pain001.core;

import com.pain001.constants.Constants;
import com.pain001.csv.CsvLoader;
import com.pain001.csv.CsvValidator;
import com.pain001.db.DatabaseLoader;
import com.pain001.xml.GenerateXml;
import com.pain001.xml.RegisterNamespaces;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class CoreProcessor {
    private static final Logger LOGGER = Logger.getLogger(CoreProcessor.class.getName());

    public static void processFiles(
            String xmlMessageType,
            String xmlTemplateFilePath,
            String xsdSchemaFilePath,
            String dataFilePath
    ) throws Exception {

        // Check if the XML message type is supported
        if (!Constants.VALID_XML_TYPES.contains(xmlMessageType)) {
            String errorMessage = "Error: Invalid XML message type: '" + xmlMessageType + "'.";
            LOGGER.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Validate file existence
        validateFileExists(xmlTemplateFilePath, "XML template");
        validateFileExists(xsdSchemaFilePath, "XSD schema");
        validateFileExists(dataFilePath, "Data file");

        // Determine data file type
        boolean isCsv = dataFilePath.endsWith(".csv");
        boolean isSqlite = dataFilePath.endsWith(".db");
        List<Map<String, String>> data;

        if (isCsv) {
            data = CsvLoader.loadCsvData(dataFilePath);
            if (!CsvValidator.validateCsvData(data)) {
                String errorMessage = "Error: Invalid CSV data.";
                LOGGER.severe(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
        } /*else if (isSqlite) {
            data = DatabaseLoader.loadDbData(dataFilePath, "pain001");
            if (!DatabaseValidator.validateDbData(data)) {
                String errorMessage = "Error: Invalid SQLite data.";
                LOGGER.severe(errorMessage);
                throw new IllegalArgumentException(errorMessage);
            }
        }*/ else {
            String errorMessage = "Error: Unsupported data file type.";
            LOGGER.severe(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // Register namespaces
        RegisterNamespaces.registerNamespaces(xmlMessageType);

        // Generate XML file
        GenerateXml.generateXml(data, xmlMessageType, xmlTemplateFilePath, xsdSchemaFilePath);

        // Confirm XML file generation
        if (new File(xmlTemplateFilePath).exists()) {
            LOGGER.info("Successfully generated XML file: " + xmlTemplateFilePath);
        } else {
            LOGGER.severe("Failed to generate XML file at: " + xmlTemplateFilePath);
        }
    }

    private static void validateFileExists(String filePath, String fileType) throws Exception {
        File file = new File(filePath);
        if (!file.exists()) {
            String errorMessage = "Error: " + fileType + " '" + filePath + "' does not exist.";
            LOGGER.severe(errorMessage);
            throw new Exception(errorMessage);
        }
    }
}
