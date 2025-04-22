package com.pain001.xml;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;

/**
 * Utility class for validating XML files against XSD schemas.
 */
public class XmlValidator {

    /**
     * Validates an XML file against an XSD schema.
     *
     * @param xmlFilePath Path to the XML file to validate
     * @param xsdFilePath Path to the XSD schema file
     * @return true if the XML is valid according to the schema, false otherwise
     */
    public static boolean validateViaXsd(String xmlFilePath, String xsdFilePath) {
        try {
            // Load the XML Schema (XSD)
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdFilePath));

            // Create Validator
            Validator validator = schema.newValidator();

            // Validate XML against XSD
            validator.validate(new StreamSource(new File(xmlFilePath)));

            System.out.println("XML is valid.");
            return true;

        } catch (SAXException e) {
            System.err.println("XML Validation Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
        }
        return false;
    }
}