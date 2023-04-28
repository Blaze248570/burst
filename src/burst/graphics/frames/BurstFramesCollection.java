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

        BurstFrame texFrame = new BurstFrame(graphic, name);
        texFrame.name = name;
        texFrame.sourceSize.setLocation(sourceSize.x, sourceSize.y);
        texFrame.offset.setLocation(offset.x, offset.y);
        texFrame.checkFrame();

        return pushFrame(texFrame);
    }

    public BurstFrame pushFrame(BurstFrame frame) 
    {
        String name = frame.name;
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        this.add(frame);
        framesHash.put(name, frame);

        return frame;
    }

    @Override
    public String toString()
    {
        String print = "BurstFrameCollection ~ [\n";

        for(BurstFrame frame : this)
        {
            print += "\t" + frame.toString() + ",\n";
        }

        return print.substring(0, print.length() - 2) + "\n]";
    }
}