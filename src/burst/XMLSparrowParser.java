package burst;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLSparrowParser {
    public static ArrayList<XMLData> parseXML(String path) {
        path += path.endsWith(".xml") ? "" : ".xml";

        ArrayList<XMLData> XMLList = new ArrayList<>();

        String[] vars = {"name", "x", "y", "width", "height", "pivotX", "pivotY", "frameX", "frameY", "frameWidth", "frameHeight"};

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document d = db.parse(new File(path));
            d.getDocumentElement().normalize();

            NodeList nodeList = d.getElementsByTagName("SubTexture");
            for(int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                XMLData XML = new XMLData();
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    for(String var : vars) {
                        String xmlvar = e.getAttribute(var);
                        if(!xmlvar.isEmpty()) {
                            switch(var) {
                                case "name":
                                    XML.name = xmlvar;
                                    break;
                                case "x":
                                    XML.x = Integer.parseInt(xmlvar);
                                    break;
                                case "y":
                                    XML.y = Integer.parseInt(xmlvar);
                                    break;
                                case "width":
                                    XML.width = Integer.parseInt(xmlvar);
                                    break;
                                case "height":
                                    XML.height = Integer.parseInt(xmlvar);
                                    break;
                                case "pivotX":
                                    XML.pivotX = Float.parseFloat(xmlvar);
                                    break;
                                case "pivotY":
                                    XML.pivotY = Float.parseFloat(xmlvar);
                                    break;
                                case "frameX":
                                    XML.frameX = Integer.parseInt(xmlvar);
                                    break;
                                case "frameY":
                                    XML.frameY = Integer.parseInt(xmlvar);
                                    break;
                                case "frameWidth":
                                    XML.frameWidth = Integer.parseInt(xmlvar);
                                    break;
                                case "frameHeight":
                                    XML.frameHeight = Integer.parseInt(xmlvar);
                                    break;
                            }
                        }
                    }
                }
                XMLList.add(XML);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return XMLList;
    }
}