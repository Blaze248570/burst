package javax.swing.burst.util;

import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

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

            doc
                .getDocumentElement()
                .normalize();

            return doc;
        } 
        catch (Exception e) 
        {
            System.out.println("Error parsing XML document: " + path);
        }

        return null;
    }
}