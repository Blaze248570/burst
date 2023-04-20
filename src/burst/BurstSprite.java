package burst;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;

import burst.animation.BurstAnimationController;
import burst.graphics.frames.BurstFrame;
import burst.graphics.frames.BurstFramesCollection;
import burst.graphics.BurstGraphic;

public class BurstSprite extends BurstBasic 
{
    public int x = 0;
    public int y = 0;
    public int width;
    public int height;
    public float alpha = 1.0f;

    public BurstAnimationController animation;
    public BurstFrame frame;
    public int numFrames;
    public BurstFramesCollection frames;
    public BurstGraphic graphic;
    public ImageObserver watcher;

    public BurstSprite() 
    {
        this(0, 0);
    }

    public BurstSprite(int x, int y) 
    {
        super();

        this.x = x;
        this.y = y;
        setLocation(x, y);

        this.width = 0;
        this.height = 0;

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

    public BurstSprite loadGraphic(BurstGraphic graphic, boolean animated, int width, int height) 
    {
        if(width == 0) 
        {
            width = animated ? graphic.height : graphic.width;
			width = (width > graphic.width) ? graphic.width : width;
        }

        if (height == 0)
		{
			height = animated ? width : graphic.height;
			height = (height > graphic.height) ? graphic.height : height;
		}

        this.graphic = graphic;

        if(!animated) 
        {
            this.frame = new BurstFrame(this.graphic);
            this.setSize(frame.width, frame.height);

            return this;
        }

        return this;
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

        System.out.println("I'm drawing!\n" + graphic);

        graphics.drawImage(frame.graphic.data/*.getSubimage(frame.x, frame.y, frame.width, frame.height)*/, 0, 0, watcher);
    }

    @Override
    public String toString()
    {
        return "BurstSprite ~ {x: " + this.x + ", y: " + this.y + ", width: " + this.width + ", height: " + this.height + "}";
    }
}
