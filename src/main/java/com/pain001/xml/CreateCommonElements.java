package com.pain001.xml;

import java.io.StringWriter;
import java.util.Map;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CreateCommonElements {
    private Configuration cfg;

    public CreateCommonElements() {
        cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(this.getClass(), "/templates"); // Set template directory
    }

    public String createCommonElements(Map<String, Object> dataModel) throws Exception {
        try {
            Template template = cfg.getTemplate("common_elements.ftl"); // Load FreeMarker template
            StringWriter out = new StringWriter();
            template.process(dataModel, out); // Process template with data
            return out.toString();
        } catch (TemplateException e) {
            throw new Exception("Error processing FreeMarker template", e);
        }
    }
}
