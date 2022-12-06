package burst;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Xml {
    public static Document parse(String path) {
        path += path.endsWith(".xml") ? "" : ".xml";

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(new java.io.File(path));
            d.getDocumentElement().normalize();

            return d;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}