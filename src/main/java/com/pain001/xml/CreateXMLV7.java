package com.pain001.xml;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringWriter;
import java.util.Map;

public class CreateXMLV7 {
    public static Document createXmlV7(Document root, Map<String, String> data) {
        try {
            // Configure FreeMarker
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassForTemplateLoading(CreateXMLV7.class, "/templates");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // Load the FreeMarker template
            Template template = cfg.getTemplate("pain001_v7.ftl");

            // Process the template
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            String xmlContent = writer.toString();

            // Convert the FreeMarker-generated XML string into a DOM structure
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document renderedXml = builder.parse(new java.io.ByteArrayInputStream(xmlContent.getBytes()));

            // Append children from rendered XML to the root
            Element cstmrCdtTrfInitnElement = root.createElement("CstmrCdtTrfInitn");
            root.getDocumentElement().appendChild(cstmrCdtTrfInitnElement);

            Element renderedRoot = renderedXml.getDocumentElement();
            while (renderedRoot.hasChildNodes()) {
                cstmrCdtTrfInitnElement.appendChild(root.importNode(renderedRoot.getFirstChild(), true));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }
}
