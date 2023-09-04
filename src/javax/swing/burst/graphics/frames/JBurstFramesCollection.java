package javax.swing.burst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.burst.graphics.JBurstGraphic;
import javax.swing.burst.util.JBurstDestroyUtil;
import javax.swing.burst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * Base class for frame collections.
 */
public class JBurstFramesCollection implements IBurstDestroyable
{
    public ArrayList<JBurstFrame> frames = new ArrayList<>();
    /**
     * Hash of frames for this frame collection.
     */
    public HashMap<String, JBurstFrame> framesHash = new HashMap<>();

    /**
     * Graphic object this collection belongs to.
     */
    public JBurstGraphic graphic;

    public JBurstFramesCollection(JBurstGraphic graphic) 
    {
        super();
        this.graphic = graphic;
        frames.clear();
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

        frames.add(frame);
        framesHash.put(name, frame);

        return frame;
    }

    @Override 
    public void destroy() 
    { 
        frames = JBurstDestroyUtil.destroyArrayList(frames);
        framesHash = null;
        graphic = null;
    }

    @Override
    public String toString()
    {
        String print = "BurstFrameCollection ~ [\n";

        for(JBurstFrame frame : frames)
        {
            print += "\t" + frame.toString() + ",\n";
        }

        return print.substring(0, print.length() - 2) + "\n]";
    }
}