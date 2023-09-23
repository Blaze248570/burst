package burst.graphics.frames;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import burst.graphics.JBurstGraphic;
import burst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * A specialized rectangle used by the animation classes.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/graphics/frames/FlxFrame.html">FlxFrame</a>
 */
public class JBurstFrame implements IBurstDestroyable
{
    public Rectangle frame;

    /**
     * The name of this frame.
     */
    public String name;

    /**
     * The parent graphic of this frame.
     */
    public JBurstGraphic graphic;

    /**
     * Original (uncropped) image size.
     */
    public Dimension sourceSize;

    /**
     * Frame offset from the top left corner of original image.
     */
    public Point offset;

    public double angle;

    public JBurstFrame(JBurstGraphic graphic)
    {
        this(graphic, 0);
    }

    public JBurstFrame(JBurstGraphic graphic, double angle)
    {
        this.graphic = graphic;

        sourceSize = new Dimension();
        offset = new Point();
    }

    public JBurstFrame copyTo(JBurstFrame clone)
    {
        if(clone == null)
            clone = new JBurstFrame(graphic, angle);
        else
        {
            clone.graphic = graphic;
            clone.angle = angle;
            clone.frame = null;
        }

        clone.offset.setLocation(offset);
        clone.sourceSize.setSize(sourceSize);
        clone.frame = new Rectangle(frame);
        clone.name = name;

        return clone;
    }

    @Override
    public void destroy()
    {
        name = null;
        graphic = null;
        sourceSize = null;
        offset = null;
    }

    @Override
    public String toString()
    {
        return "BurstFrame ~ {name: " + name + " x: " + frame.x + ", y: " + frame.y + ", width: " + frame.width + ", height: " + frame.height + "}";
    }
}
