package com.pain001.xml;

import freemarker.template.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringWriter;
import java.util.*;

public class CreateXMLV4 {

    public static Document createXML(Document root, List<Map<String, String>> data) throws Exception {

        // Create the "CstmrCdtTrfInitn" element
        Element cstmrCdtTrfInitn = root.createElement("CstmrCdtTrfInitn");
        root.getDocumentElement().appendChild(cstmrCdtTrfInitn);

        // Configure FreeMarker
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(CreateXMLV4.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");

        // Load the FreeMarker template
        Template template = cfg.getTemplate("com/templates/pain.001.001.04/template.ftl");

        // Prepare data for rendering
        Map<String, Object> xmlData = new HashMap<>();
        xmlData.put("id", data.get(0).getOrDefault("id", ""));
        xmlData.put("date", data.get(0).getOrDefault("date", ""));
        xmlData.put("nb_of_txs", data.get(0).getOrDefault("nb_of_txs", ""));
        xmlData.put("initiator_name", data.get(0).getOrDefault("initiator_name", ""));
        xmlData.put("transactions", data.subList(1, data.size()));

        // Render the XML using FreeMarker
        StringWriter writer = new StringWriter();
        template.process(xmlData, writer);
        String xmlContent = writer.toString();

        // Convert the rendered XML string into a DOM tree
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document renderedXML = builder.parse(new java.io.ByteArrayInputStream(xmlContent.getBytes()));

        // Append the generated XML elements to the "CstmrCdtTrfInitn" node
        NodeList childNodes = renderedXML.getDocumentElement().getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node node = root.importNode(childNodes.item(i), true);
            cstmrCdtTrfInitn.appendChild(node);
        }

        return root;
    }
}
