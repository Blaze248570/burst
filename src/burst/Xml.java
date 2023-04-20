package burst;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Xml 
{
    public static Document parse(String path) 
    {
        try 
        {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new java.io.File(path + (path.endsWith(".xml") ? "" : ".xml")));
            doc.getDocumentElement().normalize();

            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }

        return null;
    }
}