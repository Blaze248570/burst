package burst.graphics.frames;

import java.awt.Rectangle;

import burst.graphics.JBurstGraphic;

public class JBurstImageFrame extends JBurstFramesCollection
{
    public static JBurstImageFrame findFrame(JBurstGraphic source, Rectangle region)
    {
        return null;
    }

    public static JBurstImageFrame fromGraphic(JBurstGraphic source, Rectangle region)
    {
        if(source == null)
            return null;

        JBurstImageFrame imageFrame = findFrame(source, region);
        if(imageFrame != null)
            return imageFrame;
        
        imageFrame= new JBurstImageFrame(source);

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

    public JBurstFrame getFrame()
    {
        return frames.get(0);
    }
}
