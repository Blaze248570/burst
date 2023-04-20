package burst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;

import burst.graphics.BurstGraphic;

public class BurstFrame extends Rectangle
{
    public String name;

    public BurstGraphic graphic;

    public Point sourceSize;
    public Point offset;

    public BurstFrame(BurstGraphic graphic) 
    {
        super(0, 0, graphic.width, graphic.height);

        this.graphic = graphic;

        this.sourceSize = new Point();
        this.offset = new Point();
    }

    /**
     * Ensures the frame isn't outside the images boundaries
     * 
     * @param frame Frame area to check
     * @param name  Frame name for debuggin info
     * @return      Checked and trimmed frame rectangle
     */
    public void checkFrame(BurstGraphic graphic, Rectangle frame, String name) 
    {
        int x = frame.x;
        if(x > graphic.data.getWidth())
            x -= (x - graphic.data.getWidth());

        int y = frame.y;
        if(y > graphic.data.getHeight())
            y -= (y - graphic.data.getHeight());

        int right = (frame.x + frame.width);
        if(right > graphic.data.getWidth())
            right -= (right - graphic.data.getWidth());

        int bottom = (frame.y + frame.height);
        if(bottom > graphic.data.getHeight())
            bottom -= (bottom - graphic.data.getHeight());

        this.setFrame(x, y, right - x, bottom - y);

        if(frame.width <= 0 || frame.height <= 0)
            System.out.println("The frame " + name + " has incorrect data and results in an image with the size of (0, 0)");
    }

    @Override
    public String toString()
    {
        return "BurstFrame ~ {x: " + x + ", y: " + y + ", width: " + width + ", height: " + height + "}";
    }
}
