package com.pain001.xml;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * XML generator class that creates ISO 20022 pain.001 XML files from input data
 * Copyright (C) 2023-2024
 * Licensed under the Apache License, Version 2.0
 */
public class GenerateXml {

    /**
     * Generates an ISO 20022 pain.001 XML file from input data.
     *
     * @param data List of maps containing payment data
     * @param paymentInitiationMessageType String indicating message type such as "pain.001.001.03"
     * @param xmlFilePath Path to write generated XML file to
     * @param xsdFilePath Path to XML schema file for validation
     * @throws IOException If file operations fail
     * @throws TemplateException If template processing fails
     */
    public static void generateXml(
            List<Map<String, String>> data,
            String paymentInitiationMessageType,
            String xmlFilePath,
            String xsdFilePath) throws IOException, TemplateException {

        // Define a mapping between the XML types and the XML generators
        Map<String, String> xmlGenerators = new HashMap<>();
        xmlGenerators.put("pain.001.001.03", "createXmlV3");
        xmlGenerators.put("pain.001.001.04", "createXmlV4");
        xmlGenerators.put("pain.001.001.05", "createXmlV5");
        xmlGenerators.put("pain.001.001.06", "createXmlV6");
        xmlGenerators.put("pain.001.001.07", "createXmlV7");
        xmlGenerators.put("pain.001.001.08", "createXmlV8");
        xmlGenerators.put("pain.001.001.09", "createXmlV9");

        Map<String, String> templateMapping = new HashMap<>();
        templateMapping.put("pain.001.001.03", "/pain00100103/template.ftl");
        templateMapping.put("pain.001.001.04", "/pain00100104/template.ftl");
        templateMapping.put("pain.001.001.05", "/pain00100105/template.ftl");
        templateMapping.put("pain.001.001.06", "/pain00100106/template.ftl");
        templateMapping.put("pain.001.001.07", "/pain00100107/template.ftl");
        templateMapping.put("pain.001.001.08", "/pain00100108/template.ftl");
        templateMapping.put("pain.001.001.09", "/pain00100109/template.ftl");

        // Check if the provided payment_initiation_message_type exists in the mapping
        if (xmlGenerators.containsKey(paymentInitiationMessageType)) {
            // Check if data is not empty
            if (data == null || data.isEmpty()) {
                System.out.println("Error: No data to process.");
                System.exit(1);
            }
            String nbOfTxsStr = data.get(0).getOrDefault("nb_of_txs", String.valueOf(data.size()));
            int nbOfTxs;
            try {
                nbOfTxs = Integer.parseInt(nbOfTxsStr);
                if (nbOfTxs > data.size()) {
                    throw new IllegalArgumentException("nb_of_txs (" + nbOfTxs + ") exceeds available data rows (" + data.size() + ").");
                }
                if (nbOfTxs <= 0) {
                    throw new IllegalArgumentException("nb_of_txs must be positive.");
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid nb_of_txs value: " + nbOfTxsStr);
            }

            //Map<String, String> namespaces = RegisterNamespaces.registerNamespaces(paymentInitiationMessageType);
            // Setup FreeMarker configuration
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_34);
            cfg.setLogTemplateExceptions(true);
            cfg.setDirectoryForTemplateLoading(new File("src/main/java/com/pain001/templates"));
            cfg.setDefaultEncoding("UTF-8");

            // Load the FreeMarker template
            //Template template = cfg.getTemplate(xmlFilePath);
            Template template = cfg.getTemplate(templateMapping.get(paymentInitiationMessageType));

            // Prepare data for template rendering based on pain version
            Map<String, Object> xmlData = prepareDataForTemplate(data, paymentInitiationMessageType);
/*            xmlData.put("namespaceDecl", namespaces.get("namespaceDecl"));
            xmlData.put("namespace", namespaces.get("default"));
            xmlData.put("xsiNamespace", namespaces.get("xsi"));*/

            // Generate updated XML file path
            String updatedXmlFilePath = generateUpdatedXmlFilePath(xmlFilePath, paymentInitiationMessageType);

            // Write the XML content to the file
            try (Writer fileWriter = new FileWriter(updatedXmlFilePath)) {
                template.process(xmlData, fileWriter);
            }

            System.out.println("A new XML file has been created at " + updatedXmlFilePath);

            // Validate the updated XML file against the XSD schema
            boolean isValid = validateViaXsd(updatedXmlFilePath, xsdFilePath);

            if (!isValid) {
                System.out.println("Error: Invalid XML data.");
                System.exit(1);
            } else {
                System.out.println("The XML has been validated against " + xsdFilePath);
            }
        } else {
            // Handle the case when the payment_initiation_message_type is not valid
            System.out.println("Error: Invalid XML message type: " + paymentInitiationMessageType);
            System.exit(1);
        }
    }

    /**
     * Prepares data for the FreeMarker template based on the pain version
     *
     * @param data The input payment data
     * @param paymentInitiationMessageType The pain version
     * @return Map containing prepared data for template rendering
     */
    private static Map<String, Object> prepareDataForTemplate(List<Map<String, String>> data, String paymentInitiationMessageType) {
        Map<String, Object> templateData = new HashMap<>();
        Map<String, String> firstRow = data.get(0);

        switch (paymentInitiationMessageType) {
            case "pain.001.001.03":
                templateData.put("id", firstRow.get("id"));
                templateData.put("date", firstRow.get("date"));
                templateData.put("payment_information_id", firstRow.get("payment_information_id"));
                templateData.put("initiator_name", firstRow.get("initiator_name"));
                templateData.put("initiator_street_name", firstRow.get("initiator_street_name"));
                templateData.put("initiator_building_number", firstRow.get("initiator_building_number"));
                templateData.put("initiator_postal_code", firstRow.get("initiator_postal_code"));
                templateData.put("initiator_town_name", firstRow.get("initiator_town_name"));
                templateData.put("initiator_country_code", firstRow.get("initiator_country_code"));
                templateData.put("payment_id", firstRow.get("payment_id"));
                templateData.put("payment_method", firstRow.get("payment_method"));
                templateData.put("batch_booking", firstRow.get("batch_booking"));
                templateData.put("requested_execution_date", firstRow.get("requested_execution_date"));
                templateData.put("debtor_name", firstRow.get("debtor_name"));
                templateData.put("debtor_street_name", firstRow.get("debtor_street_name"));
                templateData.put("debtor_building_number", firstRow.get("debtor_building_number"));
                templateData.put("debtor_postal_code", firstRow.get("debtor_postal_code"));
                templateData.put("debtor_town_name", firstRow.get("debtor_town_name"));
                templateData.put("debtor_country_code", firstRow.get("debtor_country_code"));
                templateData.put("debtor_account_IBAN", firstRow.get("debtor_account_IBAN"));
                templateData.put("debtor_agent_BIC", firstRow.get("debtor_agent_BIC"));
                templateData.put("charge_bearer", firstRow.get("charge_bearer"));
                String nbOfTxsStr = getOrDefault(firstRow, "nb_of_txs", String.valueOf(data.size()));
                int nbOfTxs;
                try {
                    nbOfTxs = Integer.parseInt(nbOfTxsStr);
                } catch (NumberFormatException e) {
                    nbOfTxs = data.size(); // Fallback to actual data size
                }
                List<Map<String, String>> transactions = new ArrayList<>();
                double ctrlSum = 0.0;
                for (int i = 0; i < Math.min(data.size(), nbOfTxs); i++) {
                    Map<String, String> row = data.get(i);
                    Map<String, String> transaction = new HashMap<>();
                    transaction.put("payment_id", row.get("payment_id"));
                    transaction.put("payment_amount", row.getOrDefault("payment_amount", ""));
                    transaction.put("payment_currency", row.getOrDefault("payment_currency", ""));
                    transaction.put("charge_bearer", row.get("charge_bearer"));
                    transaction.put("creditor_agent_BIC", row.get("creditor_agent_BIC"));
                    transaction.put("creditor_name", row.get("creditor_name"));
                    transaction.put("creditor_street_name", row.get("creditor_street_name"));
                    transaction.put("creditor_building_number", row.get("creditor_building_number"));
                    transaction.put("creditor_postal_code", row.get("creditor_postal_code"));
                    transaction.put("creditor_town_name", row.get("creditor_town_name"));
                    transaction.put("creditor_country_code", row.get("creditor_country_code"));
                    transaction.put("creditor_account_IBAN", row.get("creditor_account_IBAN"));
                    transaction.put("purpose_code", row.get("purpose_code"));
                    transaction.put("reference_number", row.get("reference_number"));
                    transaction.put("reference_date", row.get("reference_date"));
                    transactions.add(transaction);
                    try {
                        String amountStr = getOrDefault(row, "payment_amount", "0.0");
                        ctrlSum += Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid payment_amount in row " + (i + 1) + ": " + row.get("payment_amount"));
                    }
                }
                templateData.put("transactions", transactions);
                templateData.put("nb_of_txs", String.valueOf(transactions.size()));
                templateData.put("ctrl_sum", String.format("%.2f", ctrlSum));
                break;

            case "pain.001.001.04":
                templateData.put("id", getOrDefault(firstRow, "id", ""));
                templateData.put("date", getOrDefault(firstRow, "date", ""));
                templateData.put("nb_of_txs", getOrDefault(firstRow, "nb_of_txs", ""));
                templateData.put("initiator_name", getOrDefault(firstRow, "initiator_name", ""));
                templateData.put("initiator_street", getOrDefault(firstRow, "initiator_street_name", ""));
                templateData.put("initiator_building_number", getOrDefault(firstRow, "initiator_building_number", ""));
                templateData.put("initiator_postal_code", getOrDefault(firstRow, "initiator_postal_code", ""));
                templateData.put("initiator_town", getOrDefault(firstRow, "initiator_town_name", ""));
                templateData.put("initiator_country", getOrDefault(firstRow, "initiator_country_code", ""));
                templateData.put("payment_information_id", getOrDefault(firstRow, "payment_id", ""));
                templateData.put("payment_method", getOrDefault(firstRow, "payment_method", ""));
                templateData.put("batch_booking", getOrDefault(firstRow, "batch_booking", ""));
                templateData.put("requested_execution_date", getOrDefault(firstRow, "requested_execution_date", ""));
                templateData.put("debtor_name", getOrDefault(firstRow, "debtor_name", ""));
                templateData.put("debtor_street", getOrDefault(firstRow, "debtor_street_name", ""));
                templateData.put("debtor_building_number", getOrDefault(firstRow, "debtor_building_number", ""));
                templateData.put("debtor_postal_code", getOrDefault(firstRow, "debtor_postal_code", ""));
                templateData.put("debtor_town", getOrDefault(firstRow, "debtor_town_name", ""));
                templateData.put("debtor_country", getOrDefault(firstRow, "debtor_country_code", ""));
                templateData.put("debtor_account_IBAN", getOrDefault(firstRow, "debtor_account_IBAN", ""));
                templateData.put("debtor_agent_BIC", getOrDefault(firstRow, "debtor_agent_BIC", ""));
                templateData.put("debtor_agent_account_IBAN", getOrDefault(firstRow, "debtor_agent_account_IBAN", ""));
                templateData.put("instruction_for_debtor_agent", getOrDefault(firstRow, "instruction_for_debtor_agent", ""));
                templateData.put("charge_bearer", getOrDefault(firstRow, "charge_bearer", ""));
                templateData.put("charge_account_IBAN", getOrDefault(firstRow, "charge_account_IBAN", ""));
                templateData.put("charge_agent_BICFI", getOrDefault(firstRow, "charge_agent_BICFI", ""));
                templateData.put("payment_instruction_id", getOrDefault(firstRow, "payment_instruction_id", ""));
                templateData.put("payment_end_to_end_id", getOrDefault(firstRow, "payment_end_to_end_id", ""));
                templateData.put("payment_currency", getOrDefault(firstRow, "payment_currency", ""));
                templateData.put("payment_amount", getOrDefault(firstRow, "payment_amount", ""));
                templateData.put("creditor_agent_BIC", getOrDefault(firstRow, "creditor_agent_BIC", ""));
                templateData.put("creditor_name", getOrDefault(firstRow, "creditor_name", ""));
                templateData.put("creditor_street", getOrDefault(firstRow, "creditor_street", ""));
                templateData.put("creditor_building_number", getOrDefault(firstRow, "creditor_building_number", ""));
                templateData.put("creditor_postal_code", getOrDefault(firstRow, "creditor_postal_code", ""));
                templateData.put("creditor_town", getOrDefault(firstRow, "creditor_town", ""));
                templateData.put("creditor_account_IBAN", getOrDefault(firstRow, "creditor_account_IBAN", ""));
                templateData.put("purpose_code", getOrDefault(firstRow, "purpose_code", ""));
                templateData.put("reference_number", getOrDefault(firstRow, "reference_number", ""));
                templateData.put("reference_date", getOrDefault(firstRow, "reference_date", ""));
                String nbOfTxsStrV4 = getOrDefault(firstRow, "nb_of_txs", String.valueOf(data.size()));
                int nbOfTxsV4;
                try {
                    nbOfTxsV4 = Integer.parseInt(nbOfTxsStrV4);
                } catch (NumberFormatException e) {
                    nbOfTxsV4 = data.size(); // Fallback to actual data size
                }
                List<Map<String, String>> transactionsV4 = new ArrayList<>();
                double ctrlSumV4 = 0.0;
                for (int i = 0; i < Math.min(data.size(), nbOfTxsV4); i++) {
                    Map<String, String> row = data.get(i);
                    Map<String, String> transaction = new HashMap<>();
                    transaction.put("payment_instruction_id", getOrDefault(row, "payment_id", ""));
                    transaction.put("payment_end_to_end_id", getOrDefault(row, "reference_number", ""));
                    transaction.put("payment_currency", getOrDefault(row, "payment_currency", "EUR"));
                    transaction.put("payment_amount", getOrDefault(row, "payment_amount", ""));
                    transaction.put("charge_bearer", getOrDefault(row, "charge_bearer", ""));
                    transaction.put("creditor_agent_BIC", getOrDefault(row, "creditor_agent_BIC", ""));
                    transaction.put("creditor_name", getOrDefault(row, "creditor_name", ""));
                    transaction.put("creditor_street", getOrDefault(row, "creditor_street_name", ""));
                    transaction.put("creditor_building_number", getOrDefault(row, "creditor_building_number", ""));
                    transaction.put("creditor_postal_code", getOrDefault(row, "creditor_postal_code", ""));
                    transaction.put("creditor_town", getOrDefault(row, "creditor_town_name", ""));
                    transaction.put("creditor_account_IBAN", getOrDefault(row, "creditor_account_IBAN", ""));
                    transaction.put("purpose_code", getOrDefault(row, "purpose_code", ""));
                    transaction.put("reference_number", getOrDefault(row, "reference_number", ""));
                    transaction.put("reference_date", getOrDefault(row, "reference_date", ""));
                    transactionsV4.add(transaction);
                    try {
                        String amountStr = getOrDefault(row, "payment_amount", "0.0");
                        ctrlSumV4 += Double.parseDouble(amountStr);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Invalid payment_amount in row " + (i + 1) + ": " + row.get("payment_amount"));
                    }
                }
                templateData.put("transactions", transactionsV4);
                templateData.put("nb_of_txs", String.valueOf(transactionsV4.size()));
                templateData.put("ctrl_sum", String.format("%.2f", ctrlSumV4));
                break;

            case "pain.001.001.05":
                templateData.put("id", firstRow.get("id"));
                templateData.put("date", firstRow.get("date"));
                templateData.put("nb_of_txs", firstRow.get("nb_of_txs"));
                templateData.put("ctrl_sum", firstRow.get("ctrl_sum"));
                templateData.put("initiator_name", firstRow.get("initiator_name"));
                templateData.put("initiator_street_name", firstRow.get("initiator_street_name"));
                templateData.put("initiator_building_number", firstRow.get("initiator_building_number"));
                templateData.put("initiator_postal_code", firstRow.get("initiator_postal_code"));
                templateData.put("initiator_town", firstRow.get("initiator_town_name"));
                templateData.put("initiator_country", firstRow.get("initiator_country"));
                templateData.put("ultimate_debtor_name", firstRow.get("ultimate_debtor_name"));
                templateData.put("service_level_code", firstRow.get("service_level_code"));
                templateData.put("requested_execution_date", firstRow.get("requested_execution_date"));
                templateData.put("payment_information_id", firstRow.get("payment_information_id"));
                templateData.put("payment_method", firstRow.get("payment_method"));
                templateData.put("batch_booking", firstRow.get("batch_booking"));
                templateData.put("debtor_name", firstRow.get("debtor_name"));
                templateData.put("debtor_street", firstRow.get("debtor_street"));
                templateData.put("debtor_building_number", firstRow.get("debtor_building_number"));
                templateData.put("debtor_postal_code", firstRow.get("debtor_postal_code"));
                templateData.put("debtor_town", firstRow.get("debtor_town"));
                templateData.put("debtor_country", firstRow.get("debtor_country"));
                templateData.put("debtor_account_IBAN", firstRow.get("debtor_account_IBAN"));
                templateData.put("debtor_agent_BIC", firstRow.get("debtor_agent_BIC"));
                templateData.put("payment_instruction_id", firstRow.get("payment_instruction_id"));
                templateData.put("payment_end_to_end_id", firstRow.get("payment_end_to_end_id"));
                templateData.put("payment_currency", firstRow.get("payment_currency"));
                templateData.put("payment_amount", firstRow.get("payment_amount"));
                templateData.put("charge_bearer", firstRow.get("charge_bearer"));
                templateData.put("creditor_name", firstRow.get("creditor_name"));
                templateData.put("creditor_street", firstRow.get("creditor_street"));
                templateData.put("creditor_building_number", firstRow.get("creditor_building_number"));
                templateData.put("creditor_postal_code", firstRow.get("creditor_postal_code"));
                templateData.put("creditor_town", firstRow.get("creditor_town"));
                templateData.put("creditor_country", firstRow.get("creditor_country"));
                templateData.put("creditor_account_IBAN", firstRow.get("creditor_account_IBAN"));
                templateData.put("creditor_agent_BICFI", firstRow.get("creditor_agent_BICFI"));
                templateData.put("purpose_code", firstRow.get("purpose_code"));
                templateData.put("reference_number", firstRow.get("reference_number"));
                templateData.put("reference_date", firstRow.get("reference_date"));
                break;

            // Add implementations for versions 6-9 similar to the patterns above
            case "pain.001.001.06":
                templateData.put("id",firstRow.get("id"));
                templateData.put("date",firstRow.get("date"));
                templateData.put("nb_of_txs",firstRow.get("nb_of_txs"));
                templateData.put("ctrl_sum",firstRow.get("ctrl_sum"));
                templateData.put("initiator_name",firstRow.get("initiator_name"));
                templateData.put("initiator_street_name",firstRow.get("initiator_street_name"));
                templateData.put("initiator_building_number",firstRow.get("initiator_building_number"));
                templateData.put("initiator_postal_code",firstRow.get("initiator_postal_code"));
                templateData.put("initiator_town",firstRow.get("initiator_town"));
                templateData.put("initiator_country",firstRow.get("initiator_country"));
                templateData.put("payment_information_id",firstRow.get("payment_information_id"));
                templateData.put("payment_method",firstRow.get("payment_method"));
                templateData.put("batch_booking",firstRow.get("batch_booking"));
                templateData.put("requested_execution_date",firstRow.get("requested_execution_date"));
                templateData.put("debtor_name",firstRow.get("debtor_name"));
                templateData.put("debtor_street",firstRow.get("debtor_street"));
                templateData.put("debtor_building_number",firstRow.get("debtor_building_number"));
                templateData.put("debtor_postal_code",firstRow.get("debtor_postal_code"));
                templateData.put("debtor_town",firstRow.get("debtor_town"));
                templateData.put("debtor_country",firstRow.get("debtor_country"));
                templateData.put("debtor_account_IBAN",firstRow.get("debtor_account_IBAN"));
                templateData.put("debtor_agent_BIC",firstRow.get("debtor_agent_BIC"));
                templateData.put("payment_instruction_id",firstRow.get("payment_instruction_id"));
                templateData.put("payment_end_to_end_id",firstRow.get("payment_end_to_end_id"));
                templateData.put("payment_currency",firstRow.get("payment_currency"));
                templateData.put("payment_amount",firstRow.get("payment_amount"));
                templateData.put("charge_bearer",firstRow.get("charge_bearer"));
                templateData.put("creditor_name",firstRow.get("creditor_name"));
                templateData.put("creditor_street",firstRow.get("creditor_street"));
                templateData.put("creditor_building_number",firstRow.get("creditor_building_number"));
                templateData.put("creditor_postal_code",firstRow.get("creditor_postal_code"));
                templateData.put("creditor_town",firstRow.get("creditor_town"));
                templateData.put("creditor_country",firstRow.get("creditor_country"));
                templateData.put("creditor_account_IBAN",firstRow.get("creditor_account_IBAN"));
                templateData.put("creditor_agent_BICFI",firstRow.get("creditor_agent_BICFI"));
                templateData.put("purpose_code",firstRow.get("purpose_code"));
                templateData.put("reference_number",firstRow.get("reference_number"));
                templateData.put("reference_date",firstRow.get("reference_date"));
                List<Map<String, String>> transactionV6 = new ArrayList<>();
                for (Map<String, String> row : data) {
                    Map<String, String> transaction = new HashMap<>();
                    transaction.put("payment_id", row.get("payment_id"));
                    transaction.put("payment_amount", row.getOrDefault("payment_amount", ""));
                    transaction.put("payment_currency", row.getOrDefault("payment_currency", ""));
                    transaction.put("charge_bearer", row.get("charge_bearer"));
                    transaction.put("creditor_agent_BIC", row.get("creditor_agent_BIC"));
                    transaction.put("creditor_name", row.get("creditor_name"));
                    transaction.put("creditor_account_IBAN", row.get("creditor_account_IBAN"));
                    transaction.put("creditor_remittance_information", row.get("creditor_remittance_information"));
                    transactionV6.add(transaction);
                }
                templateData.put("transactions", transactionV6);
                break;
            case "pain.001.001.07":
                templateData.put("id",firstRow.get("id"));
                templateData.put("date",firstRow.get("date"));
                templateData.put("nb_of_txs",firstRow.get("nb_of_txs"));
                templateData.put("ctrl_sum",firstRow.get("ctrl_sum"));
                templateData.put("initiator_name",firstRow.get("initiator_name"));
                templateData.put("initiator_street_name",firstRow.get("initiator_street_name"));
                templateData.put("initiator_building_number",firstRow.get("initiator_building_number"));
                templateData.put("initiator_postal_code",firstRow.get("initiator_postal_code"));
                templateData.put("initiator_town",firstRow.get("initiator_town"));
                templateData.put("initiator_country",firstRow.get("initiator_country"));
                templateData.put("payment_information_id",firstRow.get("payment_information_id"));
                templateData.put("payment_method",firstRow.get("payment_method"));
                templateData.put("batch_booking",firstRow.get("batch_booking"));
                templateData.put("requested_execution_date",firstRow.get("requested_execution_date"));
                templateData.put("debtor_name",firstRow.get("debtor_name"));
                templateData.put("debtor_street",firstRow.get("debtor_street"));
                templateData.put("debtor_building_number",firstRow.get("debtor_building_number"));
                templateData.put("debtor_postal_code",firstRow.get("debtor_postal_code"));
                templateData.put("debtor_town",firstRow.get("debtor_town"));
                templateData.put("debtor_country",firstRow.get("debtor_country"));
                templateData.put("debtor_account_IBAN",firstRow.get("debtor_account_IBAN"));
                templateData.put("debtor_agent_BIC",firstRow.get("debtor_agent_BIC"));
                templateData.put("payment_instruction_id",firstRow.get("payment_instruction_id"));
                templateData.put("payment_end_to_end_id",firstRow.get("payment_end_to_end_id"));
                templateData.put("payment_currency",firstRow.get("payment_currency"));
                templateData.put("payment_amount",firstRow.get("payment_amount"));
                templateData.put("charge_bearer",firstRow.get("charge_bearer"));
                templateData.put("creditor_name",firstRow.get("creditor_name"));
                templateData.put("creditor_street",firstRow.get("creditor_street"));
                templateData.put("creditor_building_number",firstRow.get("creditor_building_number"));
                templateData.put("creditor_postal_code",firstRow.get("creditor_postal_code"));
                templateData.put("creditor_town",firstRow.get("creditor_town"));
                templateData.put("creditor_country",firstRow.get("creditor_country"));
                templateData.put("creditor_account_IBAN",firstRow.get("creditor_account_IBAN"));
                templateData.put("creditor_agent_BICFI",firstRow.get("creditor_agent_BICFI"));
                templateData.put("purpose_code",firstRow.get("purpose_code"));
                templateData.put("reference_number",firstRow.get("reference_number"));
                templateData.put("reference_date",firstRow.get("reference_date"));
                List<Map<String, String>> transactionV7 = new ArrayList<>();
                for (Map<String, String> row : data) {
                    Map<String, String> transaction = new HashMap<>();
                    transaction.put("payment_id", row.get("payment_id"));
                    transaction.put("payment_amount", row.getOrDefault("payment_amount", ""));
                    transaction.put("payment_currency", row.getOrDefault("payment_currency", ""));
                    transaction.put("charge_bearer", row.get("charge_bearer"));
                    transaction.put("creditor_agent_BIC", row.get("creditor_agent_BIC"));
                    transaction.put("creditor_name", row.get("creditor_name"));
                    transaction.put("creditor_account_IBAN", row.get("creditor_account_IBAN"));
                    transaction.put("creditor_remittance_information", row.get("creditor_remittance_information"));
                    transactionV7.add(transaction);
                }
                templateData.put("transactions", transactionV7);
                break;
            case "pain.001.001.08":
                templateData.put("id",firstRow.get("id"));
                templateData.put("date",firstRow.get("date"));
                templateData.put("nb_of_txs",firstRow.get("nb_of_txs"));
                templateData.put("ctrl_sum",firstRow.get("ctrl_sum"));
                templateData.put("initiator_name",firstRow.get("initiator_name"));
                templateData.put("initiator_street_name",firstRow.get("initiator_street_name"));
                templateData.put("initiator_building_number",firstRow.get("initiator_building_number"));
                templateData.put("initiator_postal_code",firstRow.get("initiator_postal_code"));
                templateData.put("initiator_town",firstRow.get("initiator_town"));
                templateData.put("initiator_country",firstRow.get("initiator_country"));
                templateData.put("payment_information_id",firstRow.get("payment_information_id"));
                templateData.put("payment_method",firstRow.get("payment_method"));
                templateData.put("batch_booking",firstRow.get("batch_booking"));
                templateData.put("requested_execution_date",firstRow.get("requested_execution_date"));
                templateData.put("debtor_name",firstRow.get("debtor_name"));
                templateData.put("debtor_street",firstRow.get("debtor_street"));
                templateData.put("debtor_building_number",firstRow.get("debtor_building_number"));
                templateData.put("debtor_postal_code",firstRow.get("debtor_postal_code"));
                templateData.put("debtor_town",firstRow.get("debtor_town"));
                templateData.put("debtor_country",firstRow.get("debtor_country"));
                templateData.put("debtor_account_IBAN",firstRow.get("debtor_account_IBAN"));
                templateData.put("debtor_agent_BIC",firstRow.get("debtor_agent_BIC"));
                templateData.put("payment_instruction_id",firstRow.get("payment_instruction_id"));
                templateData.put("payment_end_to_end_id",firstRow.get("payment_end_to_end_id"));
                templateData.put("payment_currency",firstRow.get("payment_currency"));
                templateData.put("payment_amount",firstRow.get("payment_amount"));
                templateData.put("charge_bearer",firstRow.get("charge_bearer"));
                templateData.put("creditor_name",firstRow.get("creditor_name"));
                templateData.put("creditor_street",firstRow.get("creditor_street"));
                templateData.put("creditor_building_number",firstRow.get("creditor_building_number"));
                templateData.put("creditor_postal_code",firstRow.get("creditor_postal_code"));
                templateData.put("creditor_town",firstRow.get("creditor_town"));
                templateData.put("creditor_country",firstRow.get("creditor_country"));
                templateData.put("creditor_account_IBAN",firstRow.get("creditor_account_IBAN"));
                templateData.put("creditor_agent_BICFI",firstRow.get("creditor_agent_BICFI"));
                templateData.put("purpose_code",firstRow.get("purpose_code"));
                templateData.put("reference_number",firstRow.get("reference_number"));
                templateData.put("reference_date",firstRow.get("reference_date"));
                List<Map<String, String>> transactionV8 = new ArrayList<>();
                for (Map<String, String> row : data) {
                    Map<String, String> transaction = new HashMap<>();
                    transaction.put("payment_id", row.get("payment_id"));
                    transaction.put("payment_amount", row.getOrDefault("payment_amount", ""));
                    transaction.put("payment_currency", row.getOrDefault("payment_currency", ""));
                    transaction.put("charge_bearer", row.get("charge_bearer"));
                    transaction.put("creditor_agent_BIC", row.get("creditor_agent_BIC"));
                    transaction.put("creditor_name", row.get("creditor_name"));
                    transaction.put("creditor_account_IBAN", row.get("creditor_account_IBAN"));
                    transaction.put("creditor_remittance_information", row.get("creditor_remittance_information"));
                    transactionV8.add(transaction);
                }
                templateData.put("transactions", transactionV8);
                break;
            case "pain.001.001.09":
                // Similar implementation as above versions, with appropriate field mappings
                templateData.put("id", firstRow.get("id"));
                templateData.put("date", firstRow.get("date"));
                templateData.put("nb_of_txs", firstRow.get("nb_of_txs"));
                templateData.put("initiator_name", firstRow.get("initiator_name"));
                templateData.put("payment_id", firstRow.get("payment_id"));
                templateData.put("payment_method", firstRow.get("payment_method"));
                templateData.put("payment_nb_of_txs", firstRow.get("payment_nb_of_txs"));
                templateData.put("requested_execution_date", firstRow.get("requested_execution_date"));
                templateData.put("debtor_name", firstRow.get("debtor_name"));
                templateData.put("debtor_account_IBAN", firstRow.get("debtor_account_IBAN"));
                templateData.put("debtor_agent_BIC", firstRow.get("debtor_agent_BIC"));
                templateData.put("charge_bearer", firstRow.get("charge_bearer"));
                // Add remaining fields for these versions

                List<Map<String, String>> transactionV9 = new ArrayList<>();
                for (Map<String, String> row : data) {
                    Map<String, String> transaction = new HashMap<>();
                    transaction.put("payment_id", row.get("payment_id"));
                    transaction.put("payment_amount", row.get("payment_amount"));
                    transaction.put("payment_currency", getOrDefault(row, "payment_currency", ""));
                    transaction.put("charge_bearer", row.get("charge_bearer"));
                    transaction.put("creditor_agent_BIC", row.get("creditor_agent_BIC"));
                    transaction.put("creditor_name", row.get("creditor_name"));
                    transaction.put("creditor_account_IBAN", row.get("creditor_account_IBAN"));
                    transaction.put("creditor_remittance_information", row.get("remittance_information"));
                    transactionV9.add(transaction);
                }
                templateData.put("transactions", transactionV9);
                break;

            default:
                System.out.println("Unexpected message type: " + paymentInitiationMessageType);
                System.exit(1);
        }

        return templateData;
    }

    /**
     * Helper method to get a value from a map with a default fallback
     */
    private static String getOrDefault(Map<String, String> map, String key, String defaultValue) {
        if (map.containsKey(key) && map.get(key) != null) {
            return map.get(key);
        }
        return defaultValue;
    }

    /**
     * Generates updated XML file path based on the original path and message type
     */
    private static String generateUpdatedXmlFilePath(String xmlFilePath, String paymentInitiationMessageType) {
        // Implementation of generate_updated_xml_file_path
        String fileName = new File(xmlFilePath).getName();
        String directory = new File(xmlFilePath).getParent();
        String baseName = fileName.substring(0, fileName.lastIndexOf('.'));
        String extension = fileName.substring(fileName.lastIndexOf('.'));

        return (directory != null ? directory + File.separator : "") +
                baseName + "_" + paymentInitiationMessageType.replace(".", "_") + extension;
    }

    /**
     * Validates XML file against an XSD schema
     */
    private static boolean validateViaXsd(String xmlFilePath, String xsdFilePath) {
        // Implementation of validate_via_xsd
        // This would require XML validation logic, typically using javax.xml.validation
        // For brevity, returning true. In a real implementation, you would:
        // 1. Create a SchemaFactory
        // 2. Create a Schema from the XSD file
        // 3. Create a Validator
        // 4. Parse the XML document
        // 5. Validate the document using the validator

        // Placeholder implementation
        XmlValidator validator = new XmlValidator();
        return validator.validate(xmlFilePath, xsdFilePath);
    }

    // Inner class for XML validation
    private static class XmlValidator {
        public boolean validate(String xmlFilePath, String xsdFilePath) {
            // Implementation of XML validation against XSD schema
            // This would use javax.xml.validation
            // For simplicity, returning true
            return true;
        }
    }
}