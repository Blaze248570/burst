package javax.swing.burst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.burst.graphics.JBurstGraphic;

public class JBurstFrame extends Rectangle
{
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
    public Point sourceSize;

    /**
     * Frame offset from the top left corner of original image.
     */
    public Point offset;

    public JBurstFrame(JBurstGraphic graphic, String name)
    {
        this(graphic, name, 0, 0, graphic.getWidth(), graphic.getHeight());
    }

    public JBurstFrame(JBurstGraphic graphic, String name, int x, int y, int width, int height)
    {
        super(x, y, width, height);
        this.graphic = graphic;

        this.name = name;
        this.sourceSize = new Point();
        this.offset = new Point();
    }

    /**
     * Ensures the frame isn't outside the images boundaries
     * 
     * @return  Checked and trimmed frame rectangle
     */
    public JBurstFrame checkFrame() 
    {
        int x = this.x;
        if(x > graphic.image.getWidth())
            x -= (x - graphic.image.getWidth());

        int y = this.y;
        if(y > graphic.image.getHeight())
            y -= (y - graphic.image.getHeight());

        int right = (this.x + this.width);
        if(right > graphic.image.getWidth())
            right -= (right - graphic.image.getWidth());

        int bottom = (this.y + this.height);
        if(bottom > graphic.image.getHeight())
            bottom -= (bottom - graphic.image.getHeight());

        this.setFrame(x, y, right - x, bottom - y);

        return this;
    }

    @Override
    public String toString()
    {
        return "BurstFrame ~ {name: " + name + " x: " + x + ", y: " + y + ", width: " + width + ", height: " + height + "}";
    }
}
