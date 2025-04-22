package com.pain001.xml;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.*;
import freemarker.template.*;

public class CreateXMLV9 {

    public static Document createXmlV9(Document root, List<Map<String, String>> data) throws Exception {
        // Create CstmrCdtTrfInitn element
        Element cstmrCdtTrfInitnElement = root.createElement("CstmrCdtTrfInitn");
        root.getDocumentElement().appendChild(cstmrCdtTrfInitnElement);

        // Configure FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("com/templates/pain.001.001.09"));
        cfg.setDefaultEncoding("UTF-8");

        Template template = cfg.getTemplate("template.xml");

        // Prepare data for FreeMarker
        Map<String, Object> xmlData = new HashMap<>();
        xmlData.put("id", data.get(0).get("id"));
        xmlData.put("date", data.get(0).get("date"));
        xmlData.put("nb_of_txs", data.get(0).get("nb_of_txs"));
        xmlData.put("initiator_name", data.get(0).get("initiator_name"));

        List<Map<String, Object>> transactions = new ArrayList<>();
        for (int i = 1; i < data.size(); i++) {
            Map<String, String> row = data.get(i);
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("payment_id", row.get("payment_id"));
            transaction.put("payment_method", row.get("payment_method"));
            transaction.put("payment_nb_of_txs", row.get("nb_of_txs"));
            transaction.put("requested_execution_date", row.get("requested_execution_date"));
            transaction.put("debtor_name", row.get("debtor_name"));
            transaction.put("debtor_account_IBAN", row.get("debtor_account_IBAN"));
            transaction.put("debtor_agent_BIC", row.get("debtor_agent_BIC"));
            transaction.put("charge_bearer", row.get("charge_bearer"));

            List<Map<String, String>> transactionDetails = new ArrayList<>();
            Map<String, String> transactionDetail = new HashMap<>();
            transactionDetail.put("payment_id", row.get("payment_id"));
            transactionDetail.put("payment_amount", row.get("payment_amount"));
            transactionDetail.put("payment_currency", row.get("currency"));
            transactionDetail.put("cdtr_agent_BICFI", row.get("creditor_agent_BIC"));
            transactionDetail.put("creditor_name", row.get("creditor_name"));
            transactionDetail.put("cdtr_account_IBAN", row.get("creditor_account_IBAN"));
            transactionDetail.put("remittance_information", row.get("remittance_information"));

            transactionDetails.add(transactionDetail);
            transaction.put("transactions", transactionDetails);
            transactions.add(transaction);
        }
        xmlData.put("transactions", transactions);

        // Render template
        StringWriter writer = new StringWriter();
        template.process(xmlData, writer);
        String xmlContent = writer.toString();

        // Convert rendered XML to DOM elements
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document renderedXml = builder.parse(new ByteArrayInputStream(xmlContent.getBytes()));

        NodeList children = renderedXml.getDocumentElement().getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node importedNode = root.importNode(children.item(i), true);
            cstmrCdtTrfInitnElement.appendChild(importedNode);
        }

        return root;
    }
}
