package com.github.jbb248.jburst.graphics.frames;

import java.awt.Rectangle;

import com.github.jbb248.jburst.graphics.JBurstGraphic;

/**
 * Simple frame collection comprising of a single frame.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/graphics/frames/FlxImageFrame.html">FlxImageFrame</a>
 */
public class JBurstImageFrame extends JBurstFramesCollection
{
    public static JBurstImageFrame fromGraphic(JBurstGraphic source, Rectangle region)
    {
        if(source == null)
            return null;

        JBurstImageFrame imageFrame = new JBurstImageFrame(source);

        if(region == null)
            region = new Rectangle(0, 0, source.getWidth(), source.getHeight());
        else
        {
            if(region.width == 0)
                region.width = source.getWidth() - region.x;

            if(region.height == 0)
                region.height = source.getHeight() - region.y;
        }

        imageFrame.addSpriteSheetFrame(region);

        return imageFrame;
    }

    public JBurstImageFrame(JBurstGraphic graphic)
    {
        super(graphic);
    }
}
