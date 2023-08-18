package burst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;

import burst.graphics.JBurstGraphic;

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
        this(graphic, name, 0, 0, graphic.width, graphic.height);
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
        if(x > graphic.data.getWidth())
            x -= (x - graphic.data.getWidth());

        int y = this.y;
        if(y > graphic.data.getHeight())
            y -= (y - graphic.data.getHeight());

        int right = (this.x + this.width);
        if(right > graphic.data.getWidth())
            right -= (right - graphic.data.getWidth());

        int bottom = (this.y + this.height);
        if(bottom > graphic.data.getHeight())
            bottom -= (bottom - graphic.data.getHeight());

        this.setFrame(x, y, right - x, bottom - y);

        return this;
    }

    @Override
    public String toString()
    {
        return "BurstFrame ~ {name: " + name + " x: " + x + ", y: " + y + ", width: " + width + ", height: " + height + "}";
    }
}
