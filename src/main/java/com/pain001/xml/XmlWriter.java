package com.pain001.xml;
import java.io.File;
import java.io.FileWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlWriter {

    /**
     * Writes an XML document to a file with pretty formatting.
     *
     * @param xmlFilePath Path to save the XML file.
     * @param rootElement The root element of the XML document.
     */
    public static void writeXmlToFile(String xmlFilePath, Element rootElement) {
        try {
            // Create a new Document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // Append the root element
            doc.appendChild(doc.importNode(rootElement, true));

            // Transform XML Document to String with Pretty Print
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            // Write XML to file
            try (FileWriter writer = new FileWriter(new File(xmlFilePath))) {
                transformer.transform(new DOMSource(doc), new StreamResult(writer));
            }

            System.out.println("XML successfully written to: " + xmlFilePath);

        } catch (Exception e) {
            System.err.println("Error writing XML to file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            // Example: Create a sample XML document
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            // Create root element
            Element root = document.createElement("Root");
            document.appendChild(root);

            // Create a child element
            Element child = document.createElement("Child");
            child.setTextContent("This is a sample XML content");
            root.appendChild(child);

            // Call the function to write XML
            writeXmlToFile("output.xml", root);

        } catch (Exception e) {
            System.err.println("Error creating XML: " + e.getMessage());
        }
    }
}
