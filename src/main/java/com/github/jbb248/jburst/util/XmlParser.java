package com.github.jbb248.jburst.util;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlParser
{
    public static Document parse(String path) 
    {
        try 
        {
            Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new java.io.File(path));

            doc.getDocumentElement().normalize();
            return doc;
        }
        catch(IOException e)
        {
            System.out.println(String.format("Error reading file: %s", path));
        }
        catch(SAXException | ParserConfigurationException e) 
        {
            System.out.println(String.format("Error parsing XML document: %s", path));
        }

        return null;
    }
}