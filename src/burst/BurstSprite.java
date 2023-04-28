package burst;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import burst.animation.BurstAnimationController;
import burst.graphics.frames.BurstAtlasFrames;
import burst.graphics.frames.BurstFrame;
import burst.graphics.frames.BurstFramesCollection;
import burst.graphics.BurstGraphic;


/**
 * <h3>
 * A sprite class extension for the Java Swing package
 * </h3>
 * 
 * <p>
 * Allows for the use of static <i>and</i> animated sprites.
 */
public class BurstSprite extends BurstBasic 
{
    public float alpha = 1.0f;

    public BurstAnimationController animation;

    public BurstGraphic graphic;

    public BurstFrame frame;
    public BurstFramesCollection frames;
    public int numFrames;

    public ImageObserver watcher;

    public BurstSprite() 
    {
        this(0, 0);
    }

    public BurstSprite(int x, int y) 
    {
        super();

        setBounds(x, y, 0, 0);

        animation = new BurstAnimationController(this);

        this.watcher = new ImageObserver()
        {
            @Override
            public boolean imageUpdate(Image img, int flags, int x, int y, int width, int height) 
            {
                if ((flags & HEIGHT) != 0)
                    System.out.println("Height: " + height );
                if ((flags & WIDTH) != 0)
                    System.out.println("Width: " + width );
                if ((flags & FRAMEBITS) != 0)
                    System.out.println("Another frame finished.");
                if ((flags & SOMEBITS) != 0)
                    System.out.println("Image section :" + new java.awt.Rectangle(x, y, width, height));
                if ((flags & ALLBITS) != 0)
                    System.out.println("Image finished!");
                if ((flags & ABORT) != 0)
                    System.out.println("Image load aborted...");
                
                return true;
            }
        };
    }

    public BurstSprite loadGraphic(BurstGraphic graphic) 
    {
        this.graphic = graphic;
        this.frames = new BurstFramesCollection(graphic);

        BurstFrame frame = new BurstFrame(this.graphic, "Frame", 0, 0, graphic.width, graphic.height);
        this.frame = frame;
        this.frames.pushFrame(frame);
        this.setSize(frame.width, frame.height);

        return this;
    }

    public BurstSprite loadAnimatedGraphic(BurstGraphic graphic, int width, int height)
    {
        if(width == 0) 
        {
            width = graphic.height;
			width = (width > graphic.width) ? graphic.width : width;
        }

        if (height == 0)
		{
			height = graphic.width;
			height = (height > graphic.height) ? graphic.height : height;
		}

        this.graphic = graphic;
        this.frames = new BurstFramesCollection(graphic);

        int x = 0;
        int y = 0;
        int inc = 0;
        while(y < graphic.height)
        {
            String frameNum = "" + inc;
            while(frameNum.length() < 4) frameNum = "0" + frameNum;
            
            BurstFrame frame = new BurstFrame(graphic, "frame" + frameNum, x, y, width, height);
            frame.checkFrame();
            frames.pushFrame(frame);

            x += width;
            if(x >= graphic.width)
            {
                x = 0;
                y += height;
            }

            inc++;
        }

        frame = frames.get(0);
        this.setSize(frame.width, frame.height);

        return this;
    }

    public BurstFramesCollection loadFrames(String graphic, String description)
    {
        return loadFrames(BurstGraphic.fromBuffImage(graphic, description), description);
    }

    public BurstFramesCollection loadFrames(BurstGraphic graphic, String description)
    {
        BurstFramesCollection frames = new BurstFramesCollection(graphic);
        
        if(description.toLowerCase().endsWith(".xml"))
            BurstAtlasFrames.fromSparrow(graphic, description);

        // Need to figure out json junk...
        // if(description.endsWith(".json"))
        //     BurstAtlasFrames.fromPacker(graphic, description);

        return frames;
    }

    @Override
    public void update(float elapsed) 
    {
        animation.update(elapsed);
    }

    public void setFrame(BurstFrame frame)
    {
        BurstFrame oldFrame = this.frame;
        this.frame = frame;

        firePropertyChange("frame", oldFrame, frame);

        if(oldFrame == frame) return;

        if(frame.width != oldFrame.width && frame.height != oldFrame.height)
        {
            setSize(frame.width, frame.height);
            revalidate();
        }
        
        repaint();
    }
    
    @Override 
    public void paint(Graphics graphics)
    {
        if(!visible || alpha == 0)
            return;

        graphics.drawImage(frame.graphic.data.getSubimage(frame.x, frame.y, frame.width, frame.height), 0, 0, watcher);
    }

    @Override
    public String toString()
    {
        return "BurstSprite ~ {x: " + getX() + ", y: " + getY() + ", width: " + getWidth() + ", height: " + this.getHeight() + "}";
    }
}
