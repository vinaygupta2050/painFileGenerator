package com.pain001.xml;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class CreateRootElement {
    private static final String NAMESPACE = "urn:iso:std:iso:20022:tech:xsd:";
    private static final String XSI_NAMESPACE = "http://www.w3.org/2001/XMLSchema-instance";

    public static String createRootElement(String messageType) {
        try {
            // Configure FreeMarker
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
            cfg.setClassForTemplateLoading(CreateRootElement.class, "/templates");
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);

            // Load the FreeMarker template
            Template template = cfg.getTemplate("root_element.ftl");

            // Data model for FreeMarker
            Map<String, String> dataModel = new HashMap<>();
            dataModel.put("namespace", NAMESPACE + messageType);
            dataModel.put("xsiNamespace", XSI_NAMESPACE);
            dataModel.put("schemaLocation", NAMESPACE + messageType + " " + messageType + ".xsd");

            // Process template
            StringWriter writer = new StringWriter();
            template.process(dataModel, writer);
            return writer.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
