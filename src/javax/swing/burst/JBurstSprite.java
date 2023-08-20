package javax.swing.burst;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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

    private float widthMult = 0.0f;
    private float heightMult = 0.0f;
    private int resizeHint = Image.SCALE_DEFAULT;

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

    /**
     * Whether or not the sprite's bounding box outline should be drawn or not.
     */
    public boolean showBounds = false;

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
    }

    /**
     * Loads this sprite as a rectangle of one solid color.
     * 
     * @param width     Width of rectangle
     * @param height    Height of rectangle
     * @param color     Color of rectangle
     * 
     * @return  This JBurstSprite. Useful for chaining.
     */
    public JBurstSprite makeGraphic(int width, int height, Color color)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        
        graphics.setColor(color);
        graphics.fillRect(0, 0, width - 1, height - 1);

        return loadGraphic(JBurstGraphic.fromImage(image));
    }

    /**
     * Loads a graphic onto this sprite.
     * 
     * @param graphic   Image to be loaded onto this sprite.
     * 
     * @return  This JBurstSprite. Useful for chaining.
     * @see {@link JBurstGraphic}
     */
    public JBurstSprite loadGraphic(JBurstGraphic graphic) 
    {
        this.graphic = graphic;
        this.frame = new JBurstFrame(graphic, "Frame", 0, 0, graphic.getWidth(), graphic.getHeight());
        this.frame.sourceSize.setLocation(frame.width, frame.height);

        this.frames = new JBurstFramesCollection(graphic);
        this.frames.pushFrame(frame);

        updateBounds();;
        
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
     * @see {@link JBurstGraphic}
     */
    public JBurstSprite loadAnimatedGraphic(JBurstGraphic graphic, int width, int height)
    {
        int graphWidth = graphic.getWidth();
        int graphHeight = graphic.getHeight();

        if(width == 0) 
        {
            width = graphHeight;
			width = (width > graphWidth) ? graphWidth : width;
        }

        if (height == 0)
		{
			height = graphWidth;
			height = (height > graphHeight) ? graphHeight : height;
		}

        this.graphic = graphic;
        this.frames = new JBurstFramesCollection(graphic);

        int x = 0;
        int y = 0;
        for(int i = 0; y < graphHeight; i++)
        {
            String frameNum = "" + i;
            while(frameNum.length() < 4) frameNum = "0" + frameNum;
            
            JBurstFrame frame = new JBurstFrame(graphic, "frame" + frameNum, x, y, width, height);
            frame.sourceSize.setLocation(width, height);
            frame.checkFrame();
            frames.pushFrame(frame);

            x += width;
            if(x >= graphWidth)
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

    /**
     * Sets the current frame of the sprite.
     * 
     * @param frame Frame to be set
     */
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

    public void setSizeMultiplier(float multiplier)
    {
        setSizeMultiplier(multiplier, multiplier);
    }

    public void setSizeMultiplier(float widthMult, float heightMult)
    {
        setSizeMultiplier(widthMult, heightMult, Image.SCALE_DEFAULT);
    }

    /**
     * Sets the size that this sprite's graphic should be drawn at, in pixels.
     * <p>
     * If either parameter is given a negateive number, 
     * the default size is returned to the graphic.
     * 
     * @param width     New width of graphic
     * @param height    New height of graphic
     * @param hint      The scaling algorithm to be used
     * 
     * @see  java.awt.Image#SCALE_DEFAULT
     * @see  java.awt.Image#SCALE_FAST
     * @see  java.awt.Image#SCALE_SMOOTH
     * @see  java.awt.Image#SCALE_REPLICATE
     * @see  java.awt.Image#SCALE_AREA_AVERAGING 
     */
    public void setSizeMultiplier(float widthMult, float heightMult, int hint)
    {
        if(widthMult < 0)
            this.widthMult = 1;
        else
            this.widthMult = widthMult;

        if(heightMult < 0)
            this.heightMult = 1;
        else
            this.heightMult = heightMult;
    }

    private void updateBounds()
    {
        int width = getWidth(), height = getHeight();

        setBounds(getX(), getY(), frame.sourceSize.x, frame.sourceSize.y);

        if(width != getWidth() || height != getHeight())
            revalidate();
    }

    @Override
    public void update(float elapsed) 
    {
        animation.update(elapsed);
    }
    
    @Override 
    public void paint(Graphics graphics)
    {
        if(!exists || !visible || alpha == 0)
            return;

        if(showBounds)
            graphics.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        Rectangle drawBox = new Rectangle(frame.x, frame.y, frame.width, frame.height);

        BufferedImage buffImage = frame.graphic.image.getSubimage(
            drawBox.x, 
            drawBox.y, 
            drawBox.width, 
            drawBox.height
        );

        /* Post-process image manipulation goes here. */
         
        Image image = buffImage;
        Point offset = (Point) frame.offset.clone();

        if(widthMult > 0 || heightMult > 0)
        {
            image = buffImage.getScaledInstance((int)(drawBox.width * widthMult), (int)(drawBox.height * heightMult), resizeHint);
            offset.x *= widthMult;
            offset.y *= heightMult;
        }

        /**********************************************/

        graphics.drawImage(
            image, 
            offset.x, 
            offset.y,
            null
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
