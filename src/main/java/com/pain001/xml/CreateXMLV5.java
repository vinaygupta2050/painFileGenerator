package com.pain001.xml;

import freemarker.template.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class CreateXMLV5 {

    public static Element createXmlV5(Document doc, List<Map<String, String>> data) throws Exception {
        // Create CstmrCdtTrfInitn element
        Element cstmrCdtTrfInitnElement = doc.createElement("CstmrCdtTrfInitn");
        doc.appendChild(cstmrCdtTrfInitnElement);

        // Configure FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("com/templates/pain.001.001.05"));
        cfg.setDefaultEncoding("UTF-8");

        // Load template
        Template template = cfg.getTemplate("template.xml");

        // Prepare data model
        Map<String, Object> xmlData = new HashMap<>();
        Map<String, String> firstEntry = data.get(0);
        xmlData.put("id", firstEntry.get("id"));
        xmlData.put("date", firstEntry.get("date"));
        xmlData.put("nb_of_txs", firstEntry.get("nb_of_txs"));
        xmlData.put("ctrl_sum", firstEntry.get("ctrl_sum"));
        xmlData.put("initiator_name", firstEntry.get("initiator_name"));
        xmlData.put("initiator_street_name", firstEntry.get("initiator_street_name"));
        xmlData.put("initiator_building_number", firstEntry.get("initiator_building_number"));
        xmlData.put("initiator_postal_code", firstEntry.get("initiator_postal_code"));
        xmlData.put("initiator_town", firstEntry.get("initiator_town_name"));
        xmlData.put("initiator_country", firstEntry.get("initiator_country"));
        xmlData.put("ultimate_debtor_name", firstEntry.get("ultimate_debtor_name"));
        xmlData.put("service_level_code", firstEntry.get("service_level_code"));
        xmlData.put("requested_execution_date", firstEntry.get("requested_execution_date"));
        xmlData.put("payment_information_id", firstEntry.get("payment_information_id"));
        xmlData.put("payment_method", firstEntry.get("payment_method"));
        xmlData.put("batch_booking", firstEntry.get("batch_booking"));
        xmlData.put("debtor_name", firstEntry.get("debtor_name"));
        xmlData.put("debtor_street", firstEntry.get("debtor_street"));
        xmlData.put("debtor_building_number", firstEntry.get("debtor_building_number"));
        xmlData.put("debtor_postal_code", firstEntry.get("debtor_postal_code"));
        xmlData.put("debtor_town", firstEntry.get("debtor_town"));
        xmlData.put("debtor_country", firstEntry.get("debtor_country"));
        xmlData.put("debtor_account_IBAN", firstEntry.get("debtor_account_IBAN"));
        xmlData.put("debtor_agent_BIC", firstEntry.get("debtor_agent_BIC"));
        xmlData.put("payment_instruction_id", firstEntry.get("payment_instruction_id"));
        xmlData.put("payment_end_to_end_id", firstEntry.get("payment_end_to_end_id"));
        xmlData.put("payment_currency", firstEntry.get("payment_currency"));
        xmlData.put("payment_amount", firstEntry.get("payment_amount"));
        xmlData.put("charge_bearer", firstEntry.get("charge_bearer"));
        xmlData.put("creditor_name", firstEntry.get("creditor_name"));
        xmlData.put("creditor_street", firstEntry.get("creditor_street"));
        xmlData.put("creditor_building_number", firstEntry.get("creditor_building_number"));
        xmlData.put("creditor_postal_code", firstEntry.get("creditor_postal_code"));
        xmlData.put("creditor_town", firstEntry.get("creditor_town"));
        xmlData.put("creditor_country", firstEntry.get("creditor_country"));
        xmlData.put("creditor_account_IBAN", firstEntry.get("creditor_account_IBAN"));
        xmlData.put("creditor_agent_BICFI", firstEntry.get("creditor_agent_BICFI"));
        xmlData.put("purpose_code", firstEntry.get("purpose_code"));
        xmlData.put("reference_number", firstEntry.get("reference_number"));
        xmlData.put("reference_date", firstEntry.get("reference_date"));

        // Render template
        StringWriter writer = new StringWriter();
        template.process(xmlData, writer);
        String xmlContent = writer.toString();

        // Parse rendered XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document renderedXml = builder.parse(new ByteArrayInputStream(xmlContent.getBytes("UTF-8")));

        // Append children from rendered XML to the main document
        NodeList children = renderedXml.getDocumentElement().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node importedNode = doc.importNode(children.item(i), true);
            cstmrCdtTrfInitnElement.appendChild(importedNode);
        }

        return cstmrCdtTrfInitnElement;
    }
}