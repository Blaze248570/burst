package burst.graphics.frames;

import burst.graphics.BurstGraphic;

import java.awt.Point;
import java.awt.Rectangle;

import java.util.HashMap;

public class BurstFramesCollection extends java.util.ArrayList<BurstFrame> 
{
    public HashMap<String, BurstFrame> framesHash = new HashMap<>();

    public BurstGraphic graphic;

    public BurstFramesCollection(BurstGraphic graphic) 
    {
        super();
        this.graphic = graphic;
        this.clear();
    }

    public BurstFrame addAtlasFrame(Rectangle frame, Point sourceSize, Point offset, String name) 
    {
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        BurstFrame texFrame = new BurstFrame(graphic);
        texFrame.name = name;
        texFrame.sourceSize.setLocation(sourceSize.x, sourceSize.y);
        texFrame.offset.setLocation(offset.x, offset.y);
        texFrame.checkFrame(graphic, frame, name);

        return pushFrame(texFrame);
    }

    public BurstFrame pushFrame(BurstFrame frame) 
    {
        String name = frame.name;
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        add(frame);
        framesHash.put(name, frame);

        return frame;
    }
}