package com.pain001.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CreateXmlElement {
    /**
     * Create and append an XML element with the specified tag, text, and
     * attributes to a given parent element in the XML tree. The new element
     * becomes a child of the parent element.
     *
     * @param document   The XML Document instance.
     * @param parent     The parent XML element to which the new element will be appended.
     * @param tag        The name of the XML tag for the new element.
     * @param text       The text content to be inserted into the new XML element. Can be null.
     * @param attributes A dictionary containing the attribute names and their corresponding values.
     * @return The newly created and appended XML element.
     */
    public static Element createXmlElement(Document document, Element parent, String tag, String text, java.util.Map<String, String> attributes) {
        Element element = document.createElement(tag);

        if (text != null) {
            element.setTextContent(text);
        }

        if (attributes != null) {
            for (java.util.Map.Entry<String, String> entry : attributes.entrySet()) {
                element.setAttribute(entry.getKey(), entry.getValue());
            }
        }

        parent.appendChild(element);
        return element;
    }
}
