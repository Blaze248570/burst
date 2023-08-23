package javax.swing.burst;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import javax.swing.burst.animation.JBurstAnimationController;
import javax.swing.burst.graphics.JBurstGraphic;
import javax.swing.burst.graphics.frames.JBurstAtlasFrames;
import javax.swing.burst.graphics.frames.JBurstFrame;
import javax.swing.burst.graphics.frames.JBurstFramesCollection;

/**
 * A sprite class extension for the Java Swing package.
 * <p>
 * Allows for the use of static <i>and</i> animated sprites.
 */
public class JBurstSprite extends JBurstBasic 
{
    /**
     * The transparency of this sprite.
     * <p> <i>Currently unused</i>
     */
    public double alpha = 1.0f;

    public double angle = 0.0;

    /**
     * The manager to control animation property's of this sprite.
     * <p> Use functions from this to add and play animations.
     */
    public final JBurstAnimationController animation;

    /**
     * Graphic used by drawing.
     */
    private JBurstGraphic graphic;

    private final Point2D.Double scale;

    private int scalingHint = Image.SCALE_DEFAULT;

    public boolean antialiasing = false;

    /**
     * A collection of all the frames used by this sprite.
     * <p>
     * Public access is provided for the sake of the animation classes, 
     * but it is strongly suggested that it be treated as <strong>read-only</strong>.
     */
    private JBurstFramesCollection frames;

    /**
     * The current frame being used in the drawing process.
     */
    private JBurstFrame frame;

    /**
     * Whether or not the sprite's bounding box outline should be drawn.
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

        scale = new Point2D.Double(1.0, 1.0);
        animation = new JBurstAnimationController(this);
    }

    @Override
    public void update(double elapsed)
    {
        animation.update(elapsed);
    }
    
    @Override 
    public void paint(Graphics g)
    {
        if(!exists || !visible || alpha == 0)
            return;

        Graphics2D graphics = (Graphics2D) g;

        Rectangle drawBox = new Rectangle(frame.x, frame.y, frame.width, frame.height);

        BufferedImage image = frame.graphic.image.getSubimage(
            drawBox.x, 
            drawBox.y, 
            drawBox.width, 
            drawBox.height
        );

        /* Post-process image manipulation */
         
        Point offset = new Point(frame.offset);

        if(scale != null && (scale.x != 1.0 || scale.y != 1.0))
        {
            image = toBufferedImage(image.getScaledInstance((int)(getFrameWidth() * scale.x), (int)(getFrameHeight() * scale.y), scalingHint));
            offset.setLocation(offset.x * scale.x, offset.y * scale.y);
        }

        // reverse(image.createGraphics(), flipX, flipY);

        /************************************/

        if(showBounds)
            graphics.drawRect(0, 0, (int) getWidth() - 1, (int) getHeight() - 1);

        AffineTransform transform = AffineTransform.getRotateInstance(angle, angle, image.getWidth() / 2, image.getHeight() / 2);

        graphics.transform(transform);

        if(antialiasing)
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        graphics.drawImage(
            image, 
            offset.x, 
            offset.y,
            null
        );
    }

    /**
     * Converts an Image into a BufferedImage.
     * 
     * @param image Image to be converted
     * 
     * @return  Converted image
     */
    private BufferedImage toBufferedImage(Image image)
    {
        if(image instanceof BufferedImage)
            return (BufferedImage) image;

        BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        return bImage;
    }

    /*
        private void reverse(Graphics2D pixels, boolean flipX, boolean flipY)
        {
            
        }
    */

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
        graphics.dispose();

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

        setFrame(new JBurstFrame(graphic, "Frame", 0, 0, graphic.getWidth(), graphic.getHeight()));
        frame.sourceSize.setLocation(frame.width, frame.height);

        this.frames = new JBurstFramesCollection(graphic);
        this.frames.pushFrame(frame);

        updateBounds();
        
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

        setFrame(frames.get(0));
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
        this.graphic = frames.graphic;
        this.frames = frames;
        this.animation.clearAnimations();

        setFrame(frames.get(0));
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

        if(oldFrame == null || oldFrame == frame) return;

        if(frame.width != oldFrame.width && frame.height != oldFrame.height)
        {
            updateBounds();
        }
    }

    /**
     * Returns a collection of all the frames used by this sprite, which may be {@code null}.
     * 
     * @return  a collection of this sprite's frames
     */
    public JBurstFramesCollection getFrames()
    {
        return frames;
    }

    /**
     * Sets the sizing scale of this sprite.
     * <p>
     * For example: providing 0.5 would halve the sprite in size.
     * <p>
     * <i>A value less then or equal to zero will be ignored.</i>
     * 
     * @param scale How big or small to make this sprite.
     */
    public void setScale(double scale)
    {
        setScale(scale, scale);
    }

    /**
     * Sets the sizing scale of this sprite.
     * <p>
     * For example: providing 0.5 to {@code scaleX} would halve the sprite in size, horizontally.
     * <p>
     * <i>Values less then or equal to zero will be ignored.</i>
     * 
     * @param scaleX    How big or small to make this sprite, horizontally.     
     * @param scaleY    How big or small to make this sprite, vertically.
     */
    public void setScale(double scaleX, double scaleY)
    {
        if(scaleX <= 0 && scaleY <= 0) return;

        scale.setLocation(scaleX, scaleY);
        updateBounds();
    }

    /**
     * Sets the size that this sprite's graphic should be drawn at, in pixels.
     * <p>
     * <i>Values less than or equal to zero will be ignored.</i>
     * 
     * @param width     New width of graphic
     * @param height    New height of graphic
     */
    public void setGraphicSize(int width, int height)
    {
        if(width <= 0 && height <= 0) return;

        double scaleX = ((double) width) / getFrameWidth();
        double scaleY = ((double) height) / getFrameHeight();

        if(width <= 0)
            scaleX = scaleY;
        else if(height <= 0)
            scaleY = scaleX;

        setScale(scaleX, scaleY);
    }

    private void updateBounds()
    {
        setBounds(getX(), getY(), getWidth(), getHeight());
        revalidate();
    }

    /**
     * Sets the method that swing uses to resize the graphic.
     * <p>
     * <i>This won't affect the sprite unless its scale has been altered.</i>
     * 
     * @param hint  Scaling method to use
     * 
     * @see  java.awt.Image#SCALE_DEFAULT
     * @see  java.awt.Image#SCALE_FAST
     * @see  java.awt.Image#SCALE_SMOOTH
     * @see  java.awt.Image#SCALE_REPLICATE
     * @see  java.awt.Image#SCALE_AREA_AVERAGING 
     */
    public void setScalingHint(int hint)
    {
        this.scalingHint = hint;
    }

    /**
     * Returns the width of this compnent with scaling calculations.
     * 
     * @return  the current width of this component
     */
    @Override 
    public int getWidth()
    {
        int width = 0;
        if(frame != null)
            width = (int) (frame.sourceSize.x * scale.x);

        return width;
    }

    /**
     * Returns the height of this compnent with scaling calculations.
     * 
     * @return  the current height of this component
     */
    @Override 
    public int getHeight()
    {
        int height = 0;
        if(frame != null)
            height = (int) (frame.sourceSize.y * scale.y);

        return height;
    }

    private int getFrameWidth()
    {
        int width = 0;
        if(frame != null)
            width = frame.width;
        
        return width;
    }

    private int getFrameHeight()
    {
        int height = 0;
        if(frame != null)
            height = frame.height;

        return height;
    }

    /**
     * Returns the width of the current frame with scaling calculations.
     * 
     * @return the current width of this frame
     */
    public double getSpriteWidth()
    {
        return getFrameWidth() * scale.x;
    }

    /**
     * Returns the height of the current frame with scaling calculations.
     * 
     * @return the current height of this frame
     */
    public double getSpriteHeight()
    {
        return getFrameHeight() * scale.y;
    }

    /**
     * Returns this sprite's graphic object, 
     * which may be {@code null}.
     * 
     * @return  this sprite's graphic object
     */
    public JBurstGraphic getGraphic()
    {
        return graphic;
    }

    /**
     * Returns a writable graphics object from this sprite's graphic.
     * 
     * @return a writable graphic object
     */
    public Graphics2D getPixels()
    {
        Graphics2D pixels = null;
        if(graphic != null)
            pixels = graphic.getPixels();
        
        return pixels;
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
