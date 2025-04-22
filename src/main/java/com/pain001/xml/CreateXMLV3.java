package com.pain001.xml;

import freemarker.template.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

public class CreateXMLV3 {
    public static Element createXMLV3(Element root, List<Map<String, String>> data) throws Exception {
        // Create "CstmrCdtTrfInitn" element
        Document doc = root.getOwnerDocument();
        Element cstmrCdtTrfInitnElement = doc.createElement("CstmrCdtTrfInitn");
        root.appendChild(cstmrCdtTrfInitnElement);

        // Configure FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("com/templates/pain.001.001.03"));
        cfg.setDefaultEncoding("UTF-8");

        // Load the template
        Template template = cfg.getTemplate("template.xml");

        // Prepare data for FreeMarker
        Map<String, Object> templateData = new HashMap<>();
        templateData.put("id", data.get(0).get("id"));
        templateData.put("date", data.get(0).get("date"));
        templateData.put("nb_of_txs", data.get(0).get("nb_of_txs"));
        templateData.put("initiator_name", data.get(0).get("initiator_name"));
        templateData.put("initiator_street_name", data.get(0).get("initiator_street_name"));
        templateData.put("initiator_building_number", data.get(0).get("initiator_building_number"));
        templateData.put("initiator_postal_code", data.get(0).get("initiator_postal_code"));
        templateData.put("initiator_town_name", data.get(0).get("initiator_town_name"));
        templateData.put("initiator_country_code", data.get(0).get("initiator_country_code"));
        templateData.put("payment_id", data.get(0).get("payment_id"));
        templateData.put("payment_method", data.get(0).get("payment_method"));
        templateData.put("batch_booking", data.get(0).get("batch_booking"));
        templateData.put("requested_execution_date", data.get(0).get("requested_execution_date"));
        templateData.put("debtor_name", data.get(0).get("debtor_name"));
        templateData.put("debtor_account_IBAN", data.get(0).get("debtor_account_IBAN"));
        templateData.put("debtor_agent_BIC", data.get(0).get("debtor_agent_BIC"));
        templateData.put("charge_bearer", data.get(0).get("charge_bearer"));

        List<Map<String, String>> transactions = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            transactions.add(data.get(i));
        }
        templateData.put("transactions", transactions);

        // Process the template
        StringWriter writer = new StringWriter();
        template.process(templateData, writer);

        // Convert generated XML string to DOM elements
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document renderedXmlDoc = builder.parse(new ByteArrayInputStream(writer.toString().getBytes()));

        Node importedNode = doc.importNode(renderedXmlDoc.getDocumentElement(), true);
        cstmrCdtTrfInitnElement.appendChild(importedNode);

        return root;
    }
}