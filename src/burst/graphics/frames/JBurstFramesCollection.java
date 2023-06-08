package burst.graphics.frames;

import burst.graphics.JBurstGraphic;

import java.awt.Point;
import java.awt.Rectangle;

import java.util.HashMap;

public class JBurstFramesCollection extends java.util.ArrayList<JBurstFrame> 
{
    public HashMap<String, JBurstFrame> framesHash = new HashMap<>();

    public JBurstGraphic graphic;

    public JBurstFramesCollection(JBurstGraphic graphic) 
    {
        super();
        this.graphic = graphic;
        this.clear();
    }

    public JBurstFrame addAtlasFrame(Rectangle frame, Point sourceSize, Point offset, String name) 
    {
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        JBurstFrame texFrame = new JBurstFrame(graphic, name, frame.x, frame.y, frame.width, frame.height);
        texFrame.sourceSize.setLocation(sourceSize.x, sourceSize.y);
        texFrame.offset.setLocation(offset.x, offset.y);
        texFrame.checkFrame();

        return pushFrame(texFrame);
    }

    public JBurstFrame pushFrame(JBurstFrame frame) 
    {
        String name = frame.name;
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        add(frame);
        framesHash.put(name, frame);

        return frame;
    }

    @Override
    public String toString()
    {
        String print = "BurstFrameCollection ~ [\n";

        for(JBurstFrame frame : this)
        {
            print += "\t" + frame.toString() + ",\n";
        }

        return print.substring(0, print.length() - 2) + "\n]";
    }
}