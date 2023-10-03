package burst;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import burst.animation.JBurstAnimationController;
import burst.graphics.JBurstGraphic;
import burst.graphics.frames.JBurstAtlasFrames;
import burst.graphics.frames.JBurstFrame;
import burst.graphics.frames.JBurstFramesCollection;
import burst.util.JBurstDestroyUtil;

/**
 * A JBurstSprite is an extended JComponent that allows for the use of animated sprites.
 * <p>
 * When a JBurstSprite is instantiated, it is automatically added to {@code JBurst.members}.
 * To undo this, use {@code setIndependent()}.
 * <p>
 * <i>Note:</i> As of now, most layout managers do not handle JBurstSprites correctly,
 * so none must be used by its container. (This can be achieved through {@code setLayout(null)})
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxSprite.html">FlxSprite</a>
 * 
 * @see JBurstAnimationController
 * @see JBurstAtlasFrames
 */
public class JBurstSprite extends JBurstBasic 
{
    /**
     * The transparency of this sprite.
     * <p> <i>Currently unused</i>
     */
    public double alpha = 1.0f;

    public boolean dirty = false;

    /**
     * Manages animation property's of this sprite.
     * <p> Use functions from this to add and play animations.
     */
    public JBurstAnimationController animation;

    private Point2D.Double scale;

    private double angle = 0.0;

    private Point _framePoint;

    private Rectangle _frameRect;

    /**
     * A collection of all the frames used by this sprite
     */
    private JBurstFramesCollection frames;

    /**
     * The current frame being used in the drawing process
     */
    private JBurstFrame _frame;

    private BufferedImage _framePixels;

    /**
     * Whether or not the sprite's bounding box outline should be painted
     */
    public boolean debugMode = false;

    /**
     * Constructs a new JBurstSprite at coordinates (0, 0),
     * so long as its container uses no layout manager.
     * Otherwise, it probably won't be anywhere.
     */
    public JBurstSprite() 
    {
        this(0, 0);
    }

    /**
     * Constructs a new JBurstSprite at coordinates ({@code x}, {@code y}),
     * so long as its container uses no layout manager.
     * Otherwise, it probably won't be anywhere.
     */
    public JBurstSprite(int x, int y) 
    {
        super();

        animation = new JBurstAnimationController(this);

        scale = new Point2D.Double(1.0, 1.0);
        _framePoint = new Point();
        _frameRect = new Rectangle();
        
        setPosition(x, y);
    }

    /**
     * Called by {@code JBurst} every "frame" unless this sprite is independent.
     * 
     * @param elapsed   time since the last call to {@code update()} in milliseconds
     */
    @Override
    public void update(int elapsed)
    {
        if(!exists || !active) return;

        super.update(elapsed);

        if(animation != null)
            animation.update(elapsed);
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
        graphics.dispose();

        return loadGraphic(JBurstGraphic.fromImage(image));
    }

    /**
     * Loads a graphic onto this sprite.
     * 
     * @param path  File path to the image to be loaded onto this sprite.
     * 
     * @return  This JBurstSprite. Useful for chaining.
     * @see {@link JBurstGraphic}
     */
    public JBurstSprite loadGraphic(String path)
    {
        return loadGraphic(JBurstGraphic.fromFile(path));
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
        setFrames(graphic.getImageFrame());
        
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
     * @param graphic       Image to be sliced and displayed
     * @param frameWidth    Width of rectangle used to slice
     * @param frameHeight   Height of rectangle used to slice
     * 
     * @return  This JBurstSprite. Useful for chaining.
     * @see {@link JBurstGraphic}
     */
    public JBurstSprite loadAnimatedGraphic(JBurstGraphic graphic, int frameWidth, int frameHeight)
    {
        // JBurst.bitmap.add(graphic);

        int graphWidth = graphic.getWidth();
        int graphHeight = graphic.getHeight();

        if(frameWidth == 0) 
        {
            frameWidth = graphHeight;
			frameWidth = (frameWidth > graphWidth) ? graphWidth : frameWidth;
        }

        if (frameHeight == 0)
		{
			frameHeight = graphWidth;
			frameHeight = (frameHeight > graphHeight) ? graphHeight : frameHeight;
		}

        JBurstFramesCollection frames = new JBurstFramesCollection(graphic);

        int numRows = graphHeight / frameHeight;
        int numCols = graphWidth / frameWidth;
        for(int i = 0; i < numRows; i++)
            for(int j = 0; j < numCols; j++)
            {
                frames.addSpriteSheetFrame(new Rectangle(j * frameWidth, i * frameHeight, frameWidth, frameHeight));
            }

        setFrames(frames);

        return this;
    }

    /**
     * This will be called whenever the sprite's graphic is loaded. 
     * It normally does nothing and is meant to be overriden.
     */
    public void graphicLoaded() { }

    /**
     * Sets the current frame of the sprite.
     * 
     * @param frame Frame to be set
     */
    public JBurstFrame setFrame(JBurstFrame frame)
    {
        if(frame != null)
        {
            // resetFrameSize();
            dirty = true;
        }
        else if(frames != null && frames.frames != null && getNumFrames() > 0)
        {
            frame = frames.frames.get(0);
            dirty = true;
        }
        else
            return null;

        _frame = frame.copyTo(_frame);
        _frameRect.setLocation(0, 0);
        _frameRect.setSize(getFrameWidth(), getFrameHeight());

        return frame;
    }

    /**
     * Loads a frame collection from a spritesheet and designated animation file.
     * 
     * @return  this sprite's frame collection
     */
    public JBurstFramesCollection setFrames(JBurstFramesCollection frames)
    {
        this.frames = frames;
        this.animation.clearAnimations();

        setFrame(frames.frames.get(0));
        updateBounds();

        return this.frames;
    }

    /**
     * Returns a collection of all the frames used by this sprite, which may be {@code null}.
     * 
     * @return  this sprite's frame collection
     */
    public JBurstFramesCollection getFrames()
    {
        return this.frames;
    }

    /**
     * Returns this sprite's angle of rotation, in radians.
     * <p> 
     * {@code Math.toDegrees()} can be used to convert 
     * this value into degrees.
     */
    public double getAngle()
    {
        return this.angle;
    }

    /**
     * Sets the angle of rotation of this sprite, in radians.
     * If you'd rather use degrees, {@code Math.toRadians()} can be used.
     * <p>
     * For example, providing {@code Math.PI} (or {@code Math.toRadians(180)}) 
     * would flip this sprite upside-down.
     * 
     * @param theta the amount to rotate this sprite by, in radians
     */
    public void setAngle(double theta)
    {
        if(theta <= -2.0 * Math.PI || theta >= 2.0 * Math.PI)
            theta %= 2.0 * Math.PI; // Keep the angle within (-2pi, 2pi) to avoid overflow/underflow errors.
        
        this.angle = theta;
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
     * @param scaleX    how big or small to make this sprite, horizontally.     
     * @param scaleY    how big or small to make this sprite, vertically.
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
     * @param width     new width of graphic
     * @param height    new height of graphic
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

    /**
     * Used by Java Swing internally to paint this sprite.
     * <p>
     * It is highly suggested that is <strong><i>not</i></strong> overriden.
     */
    @Override 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(!exists || !visible || alpha == 0) return;

        updateFramePixels();

        if(isSimpleRender())
            paintSimple(g);
        else
            paintComplex((Graphics2D) g);

        if(debugMode)
        {
            g.setColor(Color.BLACK);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
            
        g.dispose();

        updateBounds();
    }

    public boolean isSimpleRender()
    {
        return angle == 0 && scale.x == 1 && scale.y == 1;
    }

    private void paintSimple(Graphics graphics)
    {
        _frameRect.setLocation(_framePoint.x, _framePoint.y);
        _frameRect.setSize(getFrameWidth(), getFrameHeight());
        graphics.drawImage(_framePixels, 0, 0, null);
    }

    private void paintComplex(Graphics2D graphics)
    {
        AffineTransform xForm = new AffineTransform();

        int frameWidth = getFrameWidth();
        int frameHeight = getFrameHeight();

        int newWidth = (int) (Math.abs(frameWidth * Math.cos(angle)) + Math.abs(frameHeight * Math.sin(angle)));           
        int newHeight = (int) (Math.abs(frameWidth * Math.sin(angle)) + Math.abs(frameHeight * Math.cos(angle)));

        int deltaX = (newWidth - frameWidth) / 2;
        int deltaY = (newHeight - frameHeight) / 2;

        _frameRect.setLocation(_framePoint.x - (int)(deltaX * scale.x), _framePoint.y - (int)(deltaY * scale.y));
        _frameRect.setSize((int)(newWidth * scale.x), (int)(newHeight * scale.y));
        
        xForm.scale(scale.x, scale.y);
        xForm.rotate(angle, newWidth / 2, newHeight / 2);
        xForm.translate(deltaX, deltaY);

        graphics.drawImage(_framePixels, xForm, null);
    }

    private BufferedImage updateFramePixels()
    {
        if(_frame == null || !dirty) return _framePixels;

        _framePixels = _frame.paint(_framePixels);

        dirty = false;
        return _framePixels;
    }

    private void updateBounds()
    {
        setBounds(_frameRect);
        revalidate();
    }

    /**
     * Sets the x position of this sprite.
     * 
     * @param x the new x-coordinate of this sprite
     */
    public void setX(int x)
    {
        _framePoint.x = x;
    }

    /**
     * Sets the y position of this sprite.
     * 
     * @param y the new y-coordinate of this sprite
     */
    public void setY(int y)
    {
        _framePoint.y = y;
    }

    /**
     * Sets the position of this sprite.
     * <p> This should be used in opposition to {@code setLocation()} 
     * as this will also update the sprite's relative position which is used
     * in calculating rotational offsets.
     * 
     * @param x the new x-coordinate of this sprite
     * @param y the new y-coordinate of this sprite
     */
    public void setPosition(int x, int y)
    {
        _framePoint.setLocation(x, y);
    }

    /**
     * Returns the width of this sprite with scaling calculations.
     */
    public int getSpriteWidth()
    {
        if(_frame != null)
            return (int) (_frame.sourceSize.width * scale.x);

        return 0;
    }

    /**
     * Returns the height of this sprite with scaling calculations.
     */
    public int getSpriteHeight()
    {
        if(_frame != null)
            return (int) (_frame.sourceSize.height * scale.y);

        return 0;
    }

    /**
     * Returns the true width of this sprite, without scaling
     */
    public int getFrameWidth()
    {
        if(_frame != null)
            return _frame.sourceSize.width;
        
        return 0;
    }

    /**
     * Returns the true height of this sprite, without scaling
     */
    public int getFrameHeight()
    {
        if(_frame != null)
            return _frame.sourceSize.height;

        return 0;
    }

    /**
     * Returns this sprite's graphic object, which may be {@code null}.
     */
    public JBurstGraphic getGraphic()
    {
        if(frames != null)
            return frames.graphic;

        return null;
    }

    /**
     * Returns a writable graphics object from this sprite's graphic,
     * which may be {@code null}.
     */
    public Graphics2D getPixels()
    {
        JBurstGraphic graphic = getGraphic();
        if(graphic != null)
            return graphic.createGraphics();
        
        return null;
    }

    /**
     * Returns the number of frames stored within this sprite's frame collection.
     */
    public int getNumFrames()
    {
        if(frames != null && frames.frames != null)
            return frames.frames.size();

        return 0;
    }

    /**
     * Completely removes all of this sprite's tools from memory. 
     * After calling {@code destroy()}, instances of this sprite should be nulled.
     * <p>
     * <i>Warning: This sprite will no longer be usable after {@code destroy()} is called.
     * To simply deactivate this sprite, use {@code kill()}.
     * 
     * @see {@link #kill()}
     * @see {@link #revive()}
     */
    @Override
    public void destroy()
    {
        super.destroy();

        animation = JBurstDestroyUtil.destroy(animation);
        frames = JBurstDestroyUtil.destroy(frames);
        _frame = JBurstDestroyUtil.destroy(_frame);

        scale = null;
        _framePoint = null;
        _frameRect = null;
    }

    @Override
    public String toString()
    {
        return "JBurstSprite ~ {x: " + getX() + ", y: " + getY() + ", width: " + getWidth() + ", height: " + getHeight() + "}";
    }
}
