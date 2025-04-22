package com.example;

import com.pain001.core.CoreProcessor;
import com.pain001.xml.GenerateXml;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    public static void main(String args[]) throws Exception {
        //CoreProcessor coreProcessor = new CoreProcessor();
        CoreProcessor.processFiles("pain.001.001.09",
                "src/main/java/com/pain001/templates/pain00100109/template.xml",
                "src/main/java/com/pain001/templates/pain00100109/pain.001.001.09.xsd",
                "src/main/java/com/pain001/templates/pain00100109/template.csv");
    }

}
