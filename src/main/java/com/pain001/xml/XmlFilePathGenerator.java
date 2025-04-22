package com.pain001.xml;

import java.io.File;

public class XmlFilePathGenerator {

    /**
     * Generates the file path for an updated XML file.
     *
     * @param xmlFilePath The path to the original XML file.
     * @param paymentInitiationMessageType The payment message type (e.g., "pain.001.001.04").
     * @return The file path to the updated XML file.
     */
    public static String generateUpdatedXmlFilePath(String xmlFilePath, String paymentInitiationMessageType) {
        File originalFile = new File(xmlFilePath);
        File baseDirectory = originalFile.getParentFile();

        // Construct new file path
        String newFileName = paymentInitiationMessageType + ".xml";
        File newFilePath = new File(baseDirectory, newFileName);

        return newFilePath.getAbsolutePath();
    }

    public static void main(String[] args) {
        // Example usage
        String updatedPath = generateUpdatedXmlFilePath("/path/to/original.xml", "pain.001.001.04");
        System.out.println("Updated XML file path: " + updatedPath);
    }
}
