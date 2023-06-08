package burst;

import java.awt.Rectangle;
import java.awt.image.ImageObserver;

import burst.animation.JBurstAnimationController;
import burst.graphics.frames.JBurstAtlasFrames;
import burst.graphics.frames.JBurstFrame;
import burst.graphics.frames.JBurstFramesCollection;
import burst.graphics.JBurstGraphic;


/**
 * <h3>
 * A sprite class extension for the Java Swing package
 * </h3>
 * 
 * <p>
 * Allows for the use of static <i>and</i> animated sprites.
 */
public class JBurstSprite extends JBurstBasic 
{
    /**
     * The transparency of this sprite.
     * <p>
     * Currently not in use.
     */
    public float alpha = 1.0f;

    /**
     * The manager to control animation property's of this sprite.
     * <p>
     * Use functions from this to add and play animations.
     */
    public JBurstAnimationController animation;

    /**
     * Graphic used by drawing.
     * <p>
     * However, I feel it may be better to remove this with
     * due to the system I've implemented since creating this.
     */
    public JBurstGraphic graphic;

    /**
     * The current frame being used to process drawing.
     */
    public JBurstFrame frame;

    /**
     * A collection of all the frames used by this sprite.
     */
    public JBurstFramesCollection frames;

    /**
     * Manager of the image.
     * <p>
     * I frankly don't know what it does, but it's necessary to use 
     * <code>drawImage()</code> in the <code>paint()</code> function.
     */
    public ImageObserver watcher;

    /**
     * Constructs a new JBurstSprite at coordinates (0, 0);
     */
    public JBurstSprite() 
    {
        this(0, 0);
    }

    /**
     * Constructs a new JBurstSprite at coordinates 
     * (<code>x</code>, <code>y</code>).
     */
    public JBurstSprite(int x, int y) 
    {
        super();

        setBounds(x, y, 0, 0);

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
     * <p>
     * To get a JBurstGraphic from a file, use 
     * <code>JBurstGraphic.fromBuffImage()</code>.
     */
    public JBurstSprite loadGraphic(JBurstGraphic graphic) 
    {
        this.graphic = graphic;
        this.frames = new JBurstFramesCollection(graphic);

        JBurstFrame frame = new JBurstFrame(this.graphic, "Frame", 0, 0, graphic.width, graphic.height);
        this.frame = frame;
        this.frames.pushFrame(frame);
        this.setSize(frame.width, frame.height);

        return this;
    }

    /**
     * Loads a graphic onto this sprite. 
     * However, unlike <code>loadGraphic()</code>, this will give it animation properties.
     * <p>
     * This version will take the provided graphic and split it into as many frames as it can
     * with the dimensions of <code>width</code> and <code>height</code>, adding each one to the
     * list <code>frames</code>.
     * <p>
     * To use these, call <code>animation.add()</code>.
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
        int inc = 0;
        while(y < graphic.height)
        {
            String frameNum = "" + inc;
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

            inc++;
        }

        frame = frames.get(0);
        setBounds(getX(), getY(), getX() + frame.width, getY() + frame.height);

        return this;
    }

    /**
     * Loads a frame collection from a spritesheet and designated animation file.
     * <p>
     * Note: Currently only supports Sparrow encoding.
     */
    public JBurstFramesCollection loadFrames(String graphic, String animFile)
    {
        return loadFrames(JBurstGraphic.fromFile(graphic, animFile), animFile);
    }

    /**
     * Loads a frame collection from a spritesheet and designated animation file.
     * <p>
     * Note: Currently only supports Sparrow encoding.
     */
    public JBurstFramesCollection loadFrames(JBurstGraphic graphic, String animFile)
    {
        JBurstFramesCollection frames = new JBurstFramesCollection(graphic);
        
        if(animFile.toLowerCase().endsWith(".xml"))
        {
            frames = JBurstAtlasFrames.fromSparrow(graphic, animFile);
        }

        // Need to figure out jsons with java..
        // else if(description.toLowerCase().endsWith(".json"))
        //     BurstAtlasFrames.fromPacker(graphic, description);

        this.frames = frames;
        this.animation.clearFrames();

        this.frame = frames.get(0);

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
            setBounds(getX(), getY(), getX() + frame.sourceSize.x, getY() + frame.sourceSize.y);
            revalidate();
        }
        
        repaint();
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

        Rectangle drawBox = new Rectangle(frame.x, frame.y, frame.width, frame.height);

        graphics.drawImage(
            frame.graphic.data.getSubimage(
                drawBox.x, 
                drawBox.y, 
                drawBox.width, 
                drawBox.height
            ), 
            getX() + frame.offset.x, 
            getY() + frame.offset.y, 
            watcher
        );
    }

    @Override
    public String toString()
    {
        return "JBurstSprite ~ {x: " + getX() + ", y: " + getY() + ", width: " + getWidth() + ", height: " + getHeight() + "}";
    }
}
