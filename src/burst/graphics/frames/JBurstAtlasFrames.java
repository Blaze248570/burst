package burst.graphics.frames;

import burst.graphics.JBurstGraphic;

import java.awt.Rectangle;
import java.awt.Point;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JBurstAtlasFrames extends JBurstFramesCollection 
{
    public static JBurstFramesCollection fromSparrow(String graphic, String description)
    {
        return fromSparrow(JBurstGraphic.fromFile(graphic, graphic), description);
    }

    public static JBurstFramesCollection fromSparrow(JBurstGraphic graphic, String description) 
    {
        if(graphic == null || description == null || !new java.io.File(description).exists())
            return null;

        JBurstAtlasFrames frames = new JBurstAtlasFrames(graphic);
        
        NodeList data = burst.Xml.parse(description).getElementsByTagName("SubTexture");
        for(int i = 0; i < data.getLength(); i++) 
        {
            Node item = data.item(i);

            if(item.getNodeType() != Node.ELEMENT_NODE)
                continue;

            Element attribute = (Element) item;
            String name = attribute.getAttribute("name");
            boolean trimmed = (!attribute.getAttribute("frameX").isEmpty());

            Rectangle rect = 
                new Rectangle(
                    Integer.parseInt(attribute.getAttribute("x")),
                    Integer.parseInt(attribute.getAttribute("y")),
                    Integer.parseInt(attribute.getAttribute("width")),
                    Integer.parseInt(attribute.getAttribute("height"))
                );

            Rectangle size = trimmed 
            ?
                new Rectangle(
                    Integer.parseInt(attribute.getAttribute("frameX")),
                    Integer.parseInt(attribute.getAttribute("frameY")),
                    Integer.parseInt(attribute.getAttribute("frameWidth")),
                    Integer.parseInt(attribute.getAttribute("frameHeight"))
                )
            :
                new Rectangle(
                    0,
                    0,
                    rect.width,
                    rect.height
                )
            ;

            Point offset = new Point(-size.x, -size.y);
            Point sourceSize = new Point(size.width, size.height);

            frames.addAtlasFrame(rect, sourceSize, offset, name);
        }

        return frames;
    }

    public JBurstFramesCollection fromPacker(JBurstGraphic graphic, String description)
    {
        return new JBurstFramesCollection(graphic);
    }

    public JBurstAtlasFrames(JBurstGraphic graphic)
    {
        super(graphic);
    }
}
