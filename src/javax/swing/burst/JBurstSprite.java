package javax.swing.burst;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.burst.animation.JBurstAnimationController;
import javax.swing.burst.graphics.JBurstGraphic;
import javax.swing.burst.graphics.frames.JBurstAtlasFrames;
import javax.swing.burst.graphics.frames.JBurstFrame;
import javax.swing.burst.graphics.frames.JBurstFramesCollection;

/**
 * A sprite class extension for the Java Swing package.
 * 
 * <p> Allows for the use of static <i>and</i> animated sprites.
 */
public class JBurstSprite extends JBurstBasic 
{
    /**
     * The transparency of this sprite.
     * <p> Currently not in use.
     */
    public float alpha = 1.0f;

    /**
     * The manager to control animation property's of this sprite.
     * <p> Use functions from this to add and play animations.
     */
    public JBurstAnimationController animation;

    /**
     * Graphic used by drawing.
     */
    public JBurstGraphic graphic;

    /**
     * The current frame being used in the drawing process.
     */
    public JBurstFrame frame;

    /**
     * A collection of all the frames used by this sprite.
     * <p>
     * Public access is provided for the sake of the animation classes, 
     * but it is stringly suggested that it be treated as <strong>read-only</strong>.
     */
    public JBurstFramesCollection frames;

    public boolean showBounds = false;

    /**
     * Manager of the image.
     * <p>
     * I frankly don't know what it does, but it's necessary to use 
     * {@code drawImage()} in the {@code paint()} function.
     */
    private ImageObserver watcher;

    /**
     * Constructs a new JBurstSprite at coordinates (0, 0);
     */
    public JBurstSprite() 
    {
        this(0, 0);
    }

    /**
     * Constructs a new JBurstSprite at coordinates ({@code x}, {@code y}).
     */
    public JBurstSprite(int x, int y) 
    {
        super();

        setLocation(x, y);

        animation = new JBurstAnimationController(this);

        this.watcher = new ImageObserver()
        {
            @Override
            public boolean imageUpdate(java.awt.Image img, int flags, int x, int y, int width, int height) 
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

    /**
     * Loads a graphic onto this sprite to be used at drawing time.
     * <p> To get a JBurstGraphic from a file, use 
     * {@code JBurstGraphic.fromBuffImage()}.
     * 
     * @param graphic   Image to be loaded onto this sprite.
     * 
     * @return  This JBurstSprite. Useful for chaining.
     */
    public JBurstSprite loadGraphic(JBurstGraphic graphic) 
    {
        this.graphic = graphic;
        this.frames = new JBurstFramesCollection(graphic);

        JBurstFrame frame = new JBurstFrame(graphic, "Frame", 0, 0, graphic.width, graphic.height);
        this.frame = frame;
        this.frames.pushFrame(frame);
        this.setSize(frame.width, frame.height);

        return this;
    }

    /**
     * Loads a graphic onto this sprite. 
     * However, unlike {@code loadGraphic()}, this will give it animation properties.
     * <p>
     * This version will take the provided graphic and split it into as many frames as it can
     * with the dimensions of {@code width} and {@code height}, adding each one to the sprite's
     * list of frames.
     * 
     * @param graphic   Image to be sliced and displayed
     * @param width     Width of frame used to slice
     * @param height    Height of frame used to slice
     * 
     * @return  This JBurstSprite. Useful for chaining.
     */
    public JBurstSprite loadAnimatedGraphic(JBurstGraphic graphic, int width, int height)
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
        this.frames = new JBurstFramesCollection(graphic);

        int x = 0;
        int y = 0;
        for(int i = 0; y < graphic.height; i++)
        {
            String frameNum = "" + i;
            while(frameNum.length() < 4) frameNum = "0" + frameNum;
            
            JBurstFrame frame = new JBurstFrame(graphic, "frame" + frameNum, x, y, width, height);
            frame.sourceSize.setLocation(width, height);
            frame.checkFrame();
            frames.pushFrame(frame);

            x += width;
            if(x >= graphic.width)
            {
                x = 0;
                y += height;
            }
        }

        frame = frames.get(0);
        updateBounds();

        return this;
    }

    /**
     * Loads a frame collection from a spritesheet and designated animation file.
     * 
     * @param frames    Frame collection to be loaded
     */
    public JBurstFramesCollection loadFrames(JBurstAtlasFrames frames)
    {
        this.frames = frames;
        this.animation.clearAnimations();

        this.frame = frames.get(0);
        updateBounds();

        return frames;
    }

    public void setFrame(JBurstFrame frame)
    {
        JBurstFrame oldFrame = this.frame;
        this.frame = frame;

        firePropertyChange("frame", oldFrame, frame);

        if(oldFrame == frame) return;

        if(frame.width != oldFrame.width && frame.height != oldFrame.height)
        {
            updateBounds();
        }
    }

    private void updateBounds()
    {
        setBounds(getX(), getY(), frame.sourceSize.x, frame.sourceSize.y);
        revalidate();
    }

    @Override
    public void update(float elapsed) 
    {
        animation.update(elapsed);
    }
    
    @Override 
    public void paint(java.awt.Graphics graphics)
    {
        if(!visible || alpha == 0)
            return;

        if(showBounds)
            graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        Rectangle drawBox = new Rectangle(frame.x, frame.y, frame.width, frame.height);

        BufferedImage pixels = frame.graphic.data.getSubimage(
            drawBox.x, 
            drawBox.y, 
            drawBox.width, 
            drawBox.height
        );

        /*
         * Post-process image manipulation would go here.
         */

        graphics.drawImage(
            pixels, 
            frame.offset.x, 
            frame.offset.y, 
            watcher
        );
    }

    public int getNumFrames()
    {
        return frames.size();
    }

    @Override
    public String toString()
    {
        return "JBurstSprite ~ {x: " + getX() + ", y: " + getY() + ", width: " + getWidth() + ", height: " + getHeight() + "}";
    }
}
