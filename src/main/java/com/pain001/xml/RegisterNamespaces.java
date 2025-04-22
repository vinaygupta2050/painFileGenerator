package com.pain001.xml;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class RegisterNamespaces {


    //private static final Logger LOGGER = Logger.getLogger(RegisterNamespaces.class.getName());

    /**
     * Registers namespaces for the payment initiation message type.
     *
     * @param paymentInitiationMessageType The payment initiation message type (e.g., "pain.001.001.03").
     * @return A map containing the default namespace, xsi namespace, and namespace declaration string.
     */
    public static Map<String, String> registerNamespaces(String paymentInitiationMessageType) {
        if (paymentInitiationMessageType == null || paymentInitiationMessageType.trim().isEmpty()) {
            System.out.println("Payment initiation message type is null or empty");
            throw new IllegalArgumentException("Payment initiation message type cannot be null or empty");
        }

        // Create the namespace for the payment initiation message type
        String namespace = "urn:iso:std:iso:20022:tech:xsd:" + paymentInitiationMessageType;
        String xsiNamespace = "http://www.w3.org/2001/XMLSchema-instance";

        // Create the namespace declaration string for Freemarker template
        String namespaceDecl = String.format("xmlns=\"%s\" xmlns:xsi=\"%s\"", namespace, xsiNamespace);

        // Return namespaces for use in GenerateXml
        Map<String, String> namespaces = new HashMap<>();
        namespaces.put("default", namespace);
        namespaces.put("xsi", xsiNamespace);
        namespaces.put("namespaceDecl", namespaceDecl);

        System.out.println("Registered namespaces for " + paymentInitiationMessageType + ": " + namespaceDecl);
        return namespaces;
    }
}
