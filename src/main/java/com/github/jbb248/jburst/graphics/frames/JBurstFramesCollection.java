package com.github.jbb248.jburst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;

import com.github.jbb248.jburst.graphics.JBurstGraphic;
import com.github.jbb248.jburst.util.JBurstDestroyUtil;
import com.github.jbb248.jburst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * Base class for frame collections.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/graphics/frames/FlxFramesCollection.html">FlxFramesCollection</a>
 */
public class JBurstFramesCollection implements IBurstDestroyable
{
    /**
     * All of the frames stored within this frame collection
     */
    public ArrayList<JBurstFrame> frames = new ArrayList<>();

    /**
     * Hash of frames stored within this frame collection.
     */
    public HashMap<String, JBurstFrame> framesHash = new HashMap<>();

    /**
     * Graphic object this collection belongs to.
     */
    public JBurstGraphic graphic;

    public JBurstFramesCollection(JBurstGraphic graphic) 
    {
        this.graphic = graphic;
    }

    public JBurstFrame addSpriteSheetFrame(Rectangle region)
    {
        JBurstFrame frame = new JBurstFrame(graphic);
        frame.frame = checkFrame(region);
        frame.sourceSize.setSize(region.width, region.height);
        frame.offset.setLocation(0, 0);

        return pushFrame(frame);
    }

    public JBurstFrame addAtlasFrame(Rectangle region, Point sourceSize, Point offset, String name) 
    {
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        JBurstFrame frame = new JBurstFrame(graphic);
        frame.name = name;
        frame.sourceSize.setSize(sourceSize.x, sourceSize.y);
        frame.offset.setLocation(offset.x, offset.y);
        frame.frame = checkFrame(region);

        return pushFrame(frame);
    }

    /**
     * Ensures the frame isn't outside the images boundaries
     * 
     * @return  checked and trimmed frame rectangle
     */
    public Rectangle checkFrame(Rectangle rect) 
    {
        int right = graphic.getWidth() - (rect.x + rect.width);
        int bottom = graphic.getHeight() - (rect.y + rect.height);

        if(right < 0)
            rect.width += right;
        if(bottom < 0)
            rect.height += bottom;

        return rect;
    }

    public JBurstFrame pushFrame(JBurstFrame frame) 
    {
        String name = frame.name;
        if(name != null && framesHash.containsKey(name))
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
        return String.format("%s[frames=%s]", getClass().getName(), frames.toString());
    }
}