package javax.swing.burst.graphics.frames;

import java.awt.Rectangle;
import java.awt.Point;
import java.util.Comparator;
import java.util.Scanner;
import javax.swing.burst.graphics.JBurstGraphic;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * A collection of frames stored as an atlas. 
 * <p> Also includes texture atlas parsing methods.
 */
public class JBurstAtlasFrames extends JBurstFramesCollection 
{
    /**
     * Parsing method for sparrow texture atlases.
     * 
     * @param path          File location of desired spritesheet to be loaded and parsed.
     * @param description   Parsing instructions file location (Should be .xml)
     */
    public static JBurstAtlasFrames fromSparrow(String graphic, String description)
    {
        return fromSparrow(JBurstGraphic.fromFile(graphic, graphic), description);
    }

    /**
     * Parsing method for sparrow texture atlases.
     * 
     * @param graphic       Spritesheet to be parsed.
     * @param description   Parsing instructions file location (Should be .xml)
     * 
     */
    public static JBurstAtlasFrames fromSparrow(JBurstGraphic graphic, String description) 
    {
        if(graphic == null || description == null || !new java.io.File(description).exists())
            return null;

        JBurstAtlasFrames frames = new JBurstAtlasFrames(graphic);
        
        NodeList data = javax.swing.burst.Xml.parse(description).getElementsByTagName("SubTexture");
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

            Rectangle size;
            if(trimmed)
                size = new Rectangle(
                    Integer.parseInt(attribute.getAttribute("frameX")),
                    Integer.parseInt(attribute.getAttribute("frameY")),
                    Integer.parseInt(attribute.getAttribute("frameWidth")),
                    Integer.parseInt(attribute.getAttribute("frameHeight"))
                );
            else
                size = new Rectangle(
                    0,
                    0,
                    rect.width,
                    rect.height
                );

            Point offset = new Point(-size.x, -size.y);
            Point sourceSize = new Point(size.width, size.height);

            frames.addAtlasFrame(rect, sourceSize, offset, name);
        }

        return frames;
    }

    /**
     * Parsing method for texture atlases in JSON format.
     * <p> 
     * <i>Note: There is currently no way to load rotated sprites.</i>
     * 
     * @param graphic       File location of desired spritesheet to be loaded and parsed.
     * @param description   Parsing instructions file location (Should be .json)
     */
    public static JBurstAtlasFrames fromTexturePackerJson(String source, String description)
    {
        return fromTexturePackerJson(JBurstGraphic.fromFile(source, source), description);
    }

    /**
     * Parsing method for texture atlases in JSON format.
     * <p> 
     * <i>Note: There is currently no way to load rotated sprites.</i>
     * 
     * @param graphic       Spritesheet to be parsed.
     * @param description   Parsing instructions file location (Should be .json)
     */
    @SuppressWarnings("unchecked")
    public static JBurstAtlasFrames fromTexturePackerJson(JBurstGraphic graphic, String description)
    {
        if(graphic == null || description == null)
            return null;

        JBurstAtlasFrames frames = new JBurstAtlasFrames(graphic);

        JSONObject data;

        try 
        {
            StringBuilder content = new StringBuilder();
            Scanner scanner = new Scanner(new java.io.File(description));

            while(scanner.hasNextLine()) content.append(scanner.nextLine());
            scanner.close();

            trimJsonContent(content);

            data = (JSONObject) new JSONParser().parse(content.toString());
        } 
        catch(java.io.IOException e) 
        {
            System.out.println("File not found: " + description);
            return null;
        }
        catch(ParseException e)
        {
            System.out.println("Error parsing JSON file: " + description);
            return null;
        }

        JSONArray frameList;
        Object framesObj = data.get("frames");

        if(framesObj instanceof JSONArray)
        {
            frameList = (JSONArray) framesObj;
        }
        else
        {
            JSONObject frameHash = (JSONObject) framesObj;
            frameList = new JSONArray();

            for(Object key : frameHash.keySet())
            {
                JSONObject frame = (JSONObject) frameHash.get(key);
                frame.put("filename", key.toString());

                frameList.add(frame);
            }

            frameList.sort(new Comparator<JSONObject>() {
                @Override
                public int compare(JSONObject o1, JSONObject o2) 
                {
                    String name1 = o1.get("filename").toString();
                    String name2 = o2.get("filename").toString();

                    return name1.compareToIgnoreCase(name2);
                }
            });
        }

        for(int i = 0; i < frameList.size(); i++)
        {
            JSONObject frameData = (JSONObject) frameList.get(i);

            String name = frameData.get("filename").toString();
            JSONObject frame = (JSONObject) frameData.get("frame");
            // boolean rotated = (Boolean) frameData.get("rotated"); // I don't currently have a way to rotate graphics.
            // boolean trimmed = (Boolean) frameData.get("trimmed");
            JSONObject spriteSourceSize = (JSONObject) frameData.get("spriteSourceSize");
            JSONObject sourceSize = (JSONObject) frameData.get("sourceSize");

            Rectangle rect = new Rectangle(
                ((Long) frame.get("x")).intValue(),
                ((Long) frame.get("y")).intValue(),
                ((Long) frame.get("w")).intValue(),
                ((Long) frame.get("h")).intValue()
            );

            frames.addAtlasFrame(
                rect, 
                new Point(
                    ((Long) sourceSize.get("w")).intValue(), 
                    ((Long) sourceSize.get("h")).intValue()), 
                new Point(
                    ((Long) spriteSourceSize.get("x")).intValue(), 
                    ((Long) spriteSourceSize.get("y")).intValue()), 
                name
            );
        }

        return frames;
    }

    /**
     * JSONs will sometimes have weird data at the beginning and ends. 
     * <p> Of course, we don't want this.
     * 
     * @param content   JSON data to be checked.
     */
    private static void trimJsonContent(StringBuilder content)
    {
        while(content.charAt(0) != '{')
            content.deleteCharAt(0);
        while(content.charAt(content.length() - 1) != '}')
            content.deleteCharAt(content.length() - 1);
    }

    /**
     * Constructs a new JBurstAtlasFrames.
     * <p> 
     * For engine purposes, please use {@code fromSparrow()} or {@code fromPacker()} instead.
     * 
     * @param graphic   Parent graphic to be used by this frame collection.
     */
    public JBurstAtlasFrames(JBurstGraphic graphic)
    {
        super(graphic);
    }
}
