package com.github.jbb248.jburst.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

import com.github.jbb248.jburst.graphics.frames.JBurstImageFrame;
import com.github.jbb248.jburst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * Image caching and managing
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/graphics/FlxGraphic.html">FlxGraphic</a>
 */
public class JBurstGraphic implements IBurstDestroyable
{
    private static int graphEnumerator = 0;
    private static final HashMap<String, JBurstGraphic> _cache = new HashMap<>();

    /**
     * Returns a JBurstGraphic using the image file specified at {@code source}
     * 
     * @param source    path of image asset to be used in returned JBurstGraphic
     */
    public static JBurstGraphic fromFile(String source) 
    {
        return fromFile(source, generateKey());
    }

    /**
     * Returns a JBurstGraphic using the image file specified at {@code source}
     * 
     * @param source    path of image asset to be used in returned JBurstGraphic
     * @param key       unique title for this JBurstGraphic.
     */
    public static JBurstGraphic fromFile(String source, String key) 
    {
        return new JBurstGraphic(key, returnBuffImage(source));
    }

    /**
     * Returns a JBurstGraphic using the BufferedImage, {@code source}
     * 
     * @param source    image to be used in returned JBurstGraphic
     */
    public static JBurstGraphic fromImage(BufferedImage source) 
    {
        return fromImage(source, generateKey());
    }

    /**
     * Returns a JBurstGraphic using the BufferedImage, {@code source}
     * 
     * @param source    image to be used in returned JBurstGraphic
     * @param key       unique title for this JBurstGraphic
     */
    public static JBurstGraphic fromImage(BufferedImage source, String key) 
    {
        if(_cache.containsKey(key))
            return _cache.get(key);
        
        return new JBurstGraphic(key, source);
    }

    private static BufferedImage returnBuffImage(String path) 
    {
        try 
        {
            return ImageIO.read(new File(path));
        } 
        catch(IOException e) 
        {
            System.out.print("Image not found: " + path);
        }

        return null;
    }

    private static String generateKey()
    {
        return "JBurstGraphic-" + graphEnumerator++;
    }

    /**
     * Unique label used in caching system
     */
    public String key;

    /**
     * Pixel data about this graphic
     */
    public BufferedImage image;

    private JBurstImageFrame _imageFrame;

    public JBurstGraphic(String key, BufferedImage image) 
    {
        this.key = key;
        this.image = image;
    }

    public int getWidth()
    {
        if(image != null)
            return image.getWidth();

        return 0;
    }

    public int getHeight()
    {
        if(image != null)
            return image.getHeight();

        return 0;
    }

    public Graphics2D createGraphics()
    {
        if(image != null)
            return image.createGraphics();

        return null;
    }

    public JBurstImageFrame getImageFrame()
    {
        if(_imageFrame == null)
            _imageFrame = JBurstImageFrame.fromGraphic(this, new Rectangle(0, 0, getWidth(), getHeight()));

        return _imageFrame;
    }

    @Override
    public void destroy()
    {
        key = null;
        image = null;
    }

    @Override 
    public String toString()
    {
        return "BurstGraphic ~ {key: " + key + ", width: " + getWidth() + ", height: " + getHeight() + "}";
    }
}
