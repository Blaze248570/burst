package com.github.jbb248.jburst;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import com.github.jbb248.jburst.animation.JBurstAnimationController;
import com.github.jbb248.jburst.graphics.JBurstGraphic;
import com.github.jbb248.jburst.graphics.frames.JBurstAtlasFrames;
import com.github.jbb248.jburst.graphics.frames.JBurstFrame;
import com.github.jbb248.jburst.graphics.frames.JBurstFramesCollection;
import com.github.jbb248.jburst.util.JBurstDestroyUtil;

/**
 * A JBurstSprite is an extended JComponent that allows for the use of animated sprites.
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
     * Whether or not this object is painted
     */
    public boolean visible = true;

    /**
     * The transparency of this sprite
     * <p> <i>Currently unused</i>
     */
    // public double alpha = 1.0f;

    /**
     * Whether or not this sprite should render backwards
     */
    public boolean flipX = false;

    /**
     * Whether or not this sprite should render upside-down
     */
    public boolean flipY = false;

    /**
     * Whether or not this sprite's frame needs updating
     * <p> <i>Normally handled internally</i>
     */
    public boolean dirty = false;

    /**
     * Whether or not the sprite's bounding box outline should be painted
     */
    public boolean debugMode = false;

    /**
     * Manages animation property's of this sprite.
     * <p> Use functions from this to add and play animations.
     */
    public final JBurstAnimationController animation = new JBurstAnimationController(this);

    private final Point2D.Double _scale = new Point2D.Double(1.0, 1.0);

    private double _angle = 0.0;

    private final Point _framePoint;

    /**
     * A collection of all the frames used by this sprite
     */
    private JBurstFramesCollection _frames;

    /**
     * The current frame being used in the drawing process
     */
    private JBurstFrame _frame;

    private BufferedImage _framePixels;

    /**
     * Constructs a new JBurstSprite at coordinates (0, 0).
     * <p> 
     * It is recommended it is used without a layout manager.
     * Otherwise, it probably won't behave.
     */
    public JBurstSprite() 
    {
        this(0, 0);
    }

    /**
     * Constructs a new JBurstSprite at coordinates ({@code x}, {@code y}).
     * <p> 
     * It is recommended it is used without a layout manager.
     * Otherwise, it probably won't behave.
     */
    public JBurstSprite(int x, int y) 
    {
        super();

        setLocation(_framePoint = new Point(x, y));
    }

    /**
     * Called by {@code JBurst} every frame
     * 
     * @param elapsed   time since the last call to {@code update()}, in seconds
     */
    @Override
    public void update(double elapsed)
    {
        if(animation != null)
            animation.update(elapsed);
    }

    /**
     * Loads this sprite as a rectangle of one solid color
     * 
     * @param width     width of rectangle
     * @param height    height of rectangle
     * @param color     color of rectangle
     * 
     * @return  this JBurstSprite. Useful for chaining.
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
     * Loads a graphic onto this sprite and automatically calls {@code JBurstGraphic.fromFile()}
     * 
     * @param source    file path to the image to be loaded onto this sprite
     * 
     * @return  this JBurstSprite. Useful for chaining.
     */
    public JBurstSprite loadGraphic(String source)
    {
        return loadGraphic(JBurstGraphic.fromFile(source));
    }

    /**
     * Loads a graphic onto this sprite
     * 
     * @param graphic   image to be loaded onto this sprite
     * 
     * @return  this JBurstSprite. Useful for chaining
     * 
     * @see JBurstGraphic#fromFile(String) JBurstGraphic.fromFile()
     * @see JBurstGraphic#fromImage(BufferedImage) JBurstGraphic.fromImage()
     */
    public JBurstSprite loadGraphic(JBurstGraphic graphic) 
    {
        setFrames(graphic.getImageFrame());
        
        return this;
    }

    /**
     * Loads a graphic onto this sprite with animation properties 
     * and automatically calls {@code JBurstGraphic.fromFile()}. 
     * <p>
     * This version will take the provided graphic and split it into as many frames as it can
     * with the dimensions of {@code width} and {@code height}, adding each one to the sprite's
     * list of frames.
     * 
     * @param source        file path to the image to be loaded onto this sprite
     * @param frameWidth    width of rectangle used to slice
     * @param frameHeight   height of rectangle used to slice
     * 
     * @return  this JBurstSprite. Useful for chaining
     * 
     * @see JBurstGraphic#fromFile(String) JBurstGraphic.fromFile()
     * @see JBurstGraphic#fromImage(BufferedImage) JBurstGraphic.fromImage()
     */
    public JBurstSprite loadAnimatedGraphic(String source, int frameWidth, int frameHeight)
    {
        return loadAnimatedGraphic(JBurstGraphic.fromFile(source), frameWidth, frameHeight);
    }

    /**
     * Loads a graphic onto this sprite with animation properties. 
     * <p>
     * This version will take the provided graphic and split it into as many frames as it can
     * with the dimensions of {@code width} and {@code height}, adding each one to the sprite's
     * list of frames.
     * 
     * @param graphic       image to be sliced and displayed
     * @param frameWidth    width of rectangle used to slice
     * @param frameHeight   height of rectangle used to slice
     * 
     * @return  this JBurstSprite. Useful for chaining.
     * 
     * @see JBurstGraphic#fromFile(String) JBurstGraphic.fromFile()
     * @see JBurstGraphic#fromImage(BufferedImage) JBurstGraphic.fromImage()
     */
    public JBurstSprite loadAnimatedGraphic(JBurstGraphic graphic, int frameWidth, int frameHeight)
    {
        int graphWidth = graphic.getWidth();
        int graphHeight = graphic.getHeight();

        if(frameWidth == 0) 
        {
            frameWidth = graphHeight;
			frameWidth = Math.min(frameWidth, graphWidth);
        }

        if (frameHeight == 0)
		{
			frameHeight = graphWidth;
			frameHeight = Math.min(frameHeight, graphHeight);
		}

        JBurstFramesCollection frames = new JBurstFramesCollection(graphic);

        final int ROWS = graphHeight / frameHeight - 1;
        final int COLS = graphWidth / frameWidth - 1;
        int i = 0, j = 0;
        do
        {
            do
            {
                frames.addSpriteSheetFrame(new Rectangle(j * frameWidth, i * frameHeight, frameWidth, frameHeight));
            }
            while(j++ < COLS);
            j = 0;
        }
        while(i++ < ROWS);

        setFrames(frames);

        return this;
    }

    /**
     * Sets the current frame of the sprite
     * 
     * @param frame frame to be set
     */
    public JBurstFrame setFrame(JBurstFrame frame)
    {
        if(frame != null)
            dirty = true;
        else if(_frames != null && getNumFrames() > 0)
        {
            frame = _frames.frames.get(0);
            dirty = true;
        }
        else
            return null;

        _frame = frame.copyTo(_frame);

        return frame;
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
     * Returns a collection of all the frames used by this sprite, which may be {@code null}
     * 
     * @return  this sprite's frame collection
     */
    public JBurstFramesCollection getFrames()
    {
        return this._frames;
    }

    /**
     * Loads a frame collection from a spritesheet and designated animation file
     * 
     * @return  this sprite's frame collection
     */
    public JBurstFramesCollection setFrames(JBurstFramesCollection frames)
    {
        this._frames = frames;
        if(frames == null)
        {
            JBurstFrame frame = new JBurstFrame(JBurstGraphic.unknownGraphic);
            int width = frame.graphic.image.getWidth();
            int height = frame.graphic.image.getHeight();
            frame.frame = new Rectangle(width, height);
            frame.sourceSize.setSize(width, height);
            setFrame(frame);
        }
        else
            setFrame(frames.frames.get(0));

        this.animation.clearAnimations();
        setLocation(_framePoint);
        setSize(getFrameWidth(), getFrameHeight());

        graphicLoaded();
        return this._frames;
    }

    /**
     * This normally does nothing.
     * It will be called whenever the sprite's graphic is loaded and is meant to be overriden.
     */
    public void graphicLoaded() { }

    /**
     * Used by Java Swing internally to paint this sprite.
     * <p>
     * <i>It is highly suggested that is <strong>not</strong> overriden.</i>
     */
    @Override 
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(!exists || !visible /*|| alpha == 0*/) return;

        updateFramePixels();

        if(isSimpleRender())
        {
            g.drawImage(_framePixels, 0, 0, null, null);
            setLocation(_framePoint);
            setSize(getFrameWidth(), getFrameHeight());
        }
        else
            paintComplex((Graphics2D) g);

        if(debugMode)
        {
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            g.fillOval(Math.toIntExact(Math.round(getWidth() * 0.5)) - 4, Math.toIntExact(Math.round(getHeight() * 0.5)) - 4, 8, 8);
        }
            
        g.dispose();
    }

    public boolean isSimpleRender()
    {
        return _angle == 0.0 && _scale.x == 1.0 && _scale.y == 1.0;
    }

    private void paintComplex(Graphics2D graphics)
    {
        AffineTransform xForm = graphics.getTransform();

        final double WIDTH = getFrameWidth() * _scale.x;
        final double HEIGHT = getFrameHeight() * _scale.y;

        double xWidth = WIDTH;
        double xHeight = HEIGHT;
        int dx = 0;
        int dy = 0;

        if(_angle != 0.0)
        {
            xWidth = WIDTH * Math.abs(Math.cos(_angle)) + HEIGHT * Math.abs(Math.sin(_angle));           
            xHeight = WIDTH * Math.abs(Math.sin(_angle)) + HEIGHT * Math.abs(Math.cos(_angle));
            dx = Math.toIntExact(Math.round((xWidth - WIDTH) * 0.5));
            dy = Math.toIntExact(Math.round((xHeight - HEIGHT) * 0.5));

            graphics.rotate(_angle, xWidth / 2, xHeight / 2);
        }

        setLocation(_framePoint.x - dx, _framePoint.y - dy);
        setSize(Math.toIntExact(Math.round(xWidth)), Math.toIntExact(Math.round(xHeight)));
        
        graphics.translate(dx, dy);
        graphics.drawImage(_framePixels, AffineTransform.getScaleInstance(_scale.x, _scale.y), null);

        if(debugMode)
        {
            graphics.setColor(Color.RED);
            graphics.drawRect(0, 0, Math.toIntExact(Math.round(WIDTH)) - 1, Math.toIntExact(Math.round(HEIGHT)) - 1);
            graphics.fillOval(Math.toIntExact(Math.round(WIDTH * 0.5)) - 4, Math.toIntExact(Math.round(HEIGHT * 0.5)) - 4, 8, 8);
        }

        graphics.setTransform(xForm);
    }

    private BufferedImage updateFramePixels()
    {
        if(_frame == null || !dirty) return _framePixels;

        _framePixels = _frame.paint(_framePixels, checkFlipX(), checkFlipY());

        dirty = false;
        return _framePixels;
    }

    private boolean checkFlipX()
    {
        return flipX ^ animation != null && animation.curAnim != null ? animation.curAnim.flipX : false;
    }

    private boolean checkFlipY()
    {
        return flipY ^ animation != null && animation.curAnim != null ? animation.curAnim.flipY : false;
    }

    /**
     * @deprecated {@code getX()} may not return the true x-coordinate of this sprite.
     * Use {@code getSpriteX()} instead.
     * 
     * @see #getSpriteX()
     */
    @Override
    @Deprecated
    public int getX()
    {
        return super.getX();
    }

    public int getSpriteX()
    {
        return _framePoint.x;
    }

    public void setSpriteX(int x)
    {
        _framePoint.x = x;
    }

    /**
     * @deprecated {@code getY()} may not return the true y-coordinate of this sprite.
     * Use {@code getSpriteY()} instead.
     * 
     * @see #getSpriteY()
     */
    @Override
    @Deprecated
    public int getY()
    {
        return super.getY();
    }

    public int getSpriteY()
    {
        return _framePoint.x;
    }

    public void setSpriteY(int y)
    {
        _framePoint.y = y;
    }

    /**
     * @deprecated {@code getLocation()} may not return the true coordinates of this sprite.
     * Use {@code getSpriteLocation()} instead.
     * 
     * @see #getSpriteLocation()
     */
    @Override
    @Deprecated
    public Point getLocation()
    {
        return super.getLocation();
    }

    /**
     * @deprecated {@code getLocation()} may not return the true coordinates of this sprite.
     * Use {@code getSpriteLocation()} instead.
     * 
     * @see #getSpriteLocation(rv)
     */
    @Override
    @Deprecated
    public Point getLocation(Point rv)
    {
        return super.getLocation(rv);
    }

    public Point getSpriteLocation()
    {
        return new Point(_framePoint);
    }

    /**
     * Gets this sprite's location and applies the corrdinates to {@code rv}
     * <p>
     * <i>If {@code rv} is null, a new Point is returned</i>
     */
    public Point getSpriteLocation(Point rv)
    {
        if(rv == null)
            return new Point(_framePoint);
        else
        {
            rv.setLocation(_framePoint);
            return rv;
        }
    }

    /**
     * @deprecated {@code setLocation()} has no effect on this sprite.
     * Use {@code setSpriteLocation()} instead.
     * 
     * @see #setSpriteLocation(int, int) setSpriteLocation()
     */
    @Override
    @Deprecated
    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
    }

    /**
     * @deprecated {@code setLocation()} has no effect on this sprite.
     * Use {@code setSpriteLocation()} instead.
     * 
     * @see #setSpriteLocation(Point) setSpriteLocation()
     */
    @Override
    @Deprecated
    public void setLocation(Point p)
    {
        super.setLocation(p);
    }

    /**
     * Sets the position of this sprite
     * 
     * @param x the new x-coordinate of this sprite
     * @param y the new y-coordinate of this sprite
     */
    public void setSpriteLocation(int x, int y)
    {
        _framePoint.setLocation(x, y);
    }

    /**
     * Sets the position of this sprite
     * 
     * @param p the new coordinates of this sprite
     */
    public void setSpriteLocation(Point p)
    {
        _framePoint.setLocation(p.x, p.y);
    }

    /**
     * @deprecated {@code getWidth()} may not return the true width of this sprite.
     * Use {@code getSpriteWidth()} instead.
     * 
     * @see #getSpriteWidth()
     */
    @Override
    @Deprecated
    public int getWidth()
    {
        return super.getWidth();
    }

    public int getSpriteWidth()
    {
        if(_frame != null)
            return (int) (_frame.sourceSize.width * _scale.x);

        return 0;
    }

    /**
     * @deprecated {@code getHeight()} may not return the true height of this sprite.
     * Use {@code getSpriteHeight()} instead.
     * 
     * @see #getSpriteHeight()
     */
    @Override
    @Deprecated
    public int getHeight()
    {
        return super.getHeight();
    }

    public int getSpriteHeight()
    {
        if(_frame != null)
            return (int) (_frame.sourceSize.height * _scale.y);

        return 0;
    }

    /**
     * @deprecated {@code getSize()} may not return the true size of this sprite.
     * Use {@code getSpriteSize()} instead.
     * 
     * @see #getSpriteSize()
     */
    @Override
    @Deprecated
    public Dimension getSize()
    {
        return super.getSize();
    }

    /**
     * @deprecated {@code getSize()} may not return the true size of this sprite.
     * Use {@code getSpriteSize()} instead.
     * 
     * @see #getSpriteSize(Dimension) getSpriteSize()
     */
    @Override
    @Deprecated
    public Dimension getSize(Dimension rv)
    {
        return super.getSize();
    }

    public Dimension getSpriteSize()
    {
        return new Dimension(getSpriteWidth(), getSpriteHeight());
    }

     /**
     * Gets this sprite's location and applies the corrdinates to {@code rv}
     * <p>
     * <i>If {@code rv} is null, a new Point is returned</i>
     */
    public Dimension getSpriteSize(Dimension rv)
    {
        if(rv == null)
            return new Dimension(getSpriteSize());
        else
        {
            rv.setSize(getSpriteWidth(), getSpriteHeight());
            return rv;
        }
    }

    /**
     * @deprecated {@code setSize()} has no effect on this sprite.
     * Use {@code setScale()} or {@code setGraphicSize()} instead.
     * 
     * @see #setScale(double) setScale()
     * @see #setGraphicSize(int, int) setGraphicSize()
     */
    @Override
    @Deprecated
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
    }

    /**
     * @deprecated {@code setSize()} has no effect on this sprite.
     * Use {@code setScale()} or {@code setGraphicSize()} instead.
     * 
     * @see #setScale(double) setScale()
     * @see #setGraphicSize(int, int) setGraphicSize()
     */
    @Override
    @Deprecated
    public void setSize(Dimension d)
    {
        super.setSize(d);
    }

    /**
     * Sets the sizing scale of this sprite.
     * <p>
     * For example: providing 0.5 would halve the sprite in size.
     * <p>
     * <i>A value less then or equal to zero will be ignored.</i>
     * 
     * @param scale how much to scale this sprite.
     */
    public void setScale(double scale)
    {
        setScale(scale, scale);
    }

    /**
     * Sets the sizing scale of this sprite.
     * <p>
     * For example: {@code setScale(0.5, 1)} would halve the sprite in size, horizontally.
     * <p>
     * <i>Values less then or equal to zero will be ignored.</i>
     * 
     * @param scaleX    how much to scale this sprite, horizontally.     
     * @param scaleY    how much to scale this sprite, vertically.
     */
    public void setScale(double scaleX, double scaleY)
    {
        if(scaleX > 0)
            _scale.x = scaleX;
        if(scaleY > 0)
            _scale.y = scaleY;
    }

    /**
     * Sets the size that this sprite should be drawn at, in pixels.
     * <p>
     * <i>If height is less than or equal to zero, it will match width and vice versa.</i>
     * <p><i>If both arguments are less than or equal to zero, this call will be ignored.</i>
     * 
     * @param width     new width of graphic
     * @param height    new height of graphic
     */
    public void setGraphicSize(int width, int height)
    {
        if(width <= 0 && height <= 0) return;

        double scaleX = (double) width / getFrameWidth();
        double scaleY = (double) height / getFrameHeight();

        if(width <= 0)
            scaleX = scaleY;
        else if(height <= 0)
            scaleY = scaleX;

        setScale(scaleX, scaleY);
    }

    /**
     * @deprecated {@code getBounds()} may not return the true bounds of this sprite.
     */
    @Override
    @Deprecated
    public Rectangle getBounds()
    {
        return super.getBounds();
    }

    /**
     * @deprecated {@code getBounds()} may not return the true bounds of this sprite.
     */
    @Override
    @Deprecated
    public Rectangle getBounds(Rectangle rv)
    {
        return super.getBounds(rv);
    }

    /**
     * @deprecated {@code setBounds()} may produce unexpected results.
     */
    @Override
    @Deprecated
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
    }

    /**
     * @deprecated {@code setBounds()} may produce unexpected results.
     */
    @Override
    @Deprecated
    public void setBounds(Rectangle r)
    {
        super.setBounds(r);
    }

    /**
     * Returns this sprite's angle of rotation, in radians
     * <p> 
     * <i>Use {@code Math.toDegrees()} to convert this value into degrees</i>
     * <p>
     * <i>This will always return a value within a range of (-2pi, 2pi)</i>
     */
    public double getAngle()
    {
        return this._angle;
    }

    /**
     * Sets the angle of rotation, in radians, of this sprite relative to its midpoint
     * with a 3px margin of error.
     * <i>If you prefer to use degrees, use {@code Math.toRadians()}.</i>
     * <p>
     * For example, providing {@code Math.PI} (or {@code Math.toRadians(180)}) 
     * would flip this sprite upside-down.
     * <p>
     * <i>{@code theta} will be reduced if it isn't within a range of (-2pi, 2pi)</i>
     * 
     * @param theta the amount to rotate this sprite by, in radians
     */
    public void setAngle(double theta)
    {
        this._angle = (theta %= 2.0 * Math.PI);
    }

    /**
     * Returns this sprite's frame collection's graphic object, which may be {@code null}
     */
    public JBurstGraphic getGraphic()
    {
        if(_frames != null)
            return _frames.graphic;

        return null;
    }

    /**
     * Returns a writable graphics object from this sprite's graphic,
     * which may be {@code null}
     */
    public Graphics2D getPixels()
    {
        JBurstGraphic graphic = getGraphic();
        if(graphic != null)
            return graphic.createGraphics();
        
        return null;
    }

    /**
     * Returns the number of frames stored within this sprite's frame collection
     */
    public int getNumFrames()
    {
        if(_frames != null && _frames.frames != null)
            return _frames.frames.size();

        return 0;
    }

    /**
     * Completely deactivates all of this sprite's tools. 
     * After calling {@code destroy()}, instances of this sprite should be nulled.
     * <p>
     * <i>Warning: Sprites are no longer usable after {@code destroy()} is called.
     * To simply deactivate this sprite, use {@code kill()}.</i>
     * 
     * @see #kill()
     * @see #revive()
     */
    @Override
    public void destroy()
    {
        super.destroy();

        JBurstDestroyUtil.destroy(animation);
        _frames = JBurstDestroyUtil.destroy(_frames);
        _frame = JBurstDestroyUtil.destroy(_frame);
    }

    @Override
    public String toString()
    {
        return String.format("%s[x=%d,y=%d]", getClass().getName(), _framePoint.x, _framePoint.y);
    }
}
