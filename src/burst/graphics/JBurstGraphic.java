package burst.graphics;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import burst.graphics.frames.JBurstImageFrame;
import burst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/graphics/FlxGraphic.html">FlxGraphic</a>
 */
public class JBurstGraphic implements IBurstDestroyable
{
    /**
     * Returns a JBurstGraphic using the image file specified at {@code source}
     * 
     * @param source    Path of image asset to be used in returned JBurstGraphic.
     */
    public static JBurstGraphic fromFile(String source) 
    {
        return fromFile(source, generateKey());
    }

    /**
     * Returns a JBurstGraphic using the image file specified at {@code source}
     * 
     * @param source    Path of image asset to be used in returned JBurstGraphic.
     * @param key       Unique title for this JBurstGraphic.
     */
    public static JBurstGraphic fromFile(String source, String key) 
    {
        return new JBurstGraphic(key, returnBuffImage(source));
    }

    /**
     * Returns a JBurstGraphic using the BufferedImage, {@code source}.
     * 
     * @param source    Image to be used in returned JBurstGraphic.
     */
    public static JBurstGraphic fromImage(BufferedImage source) 
    {
        return fromImage(source, generateKey());
    }

    /**
     * Returns a JBurstGraphic using the BufferedImage, {@code source}.
     * 
     * @param source    Image to be used in returned JBurstGraphic.
     * @param key       Unique title for this JBurstGraphic.
     */
    public static JBurstGraphic fromImage(BufferedImage source, String key) 
    {
        return new JBurstGraphic(key, source);
    }

    public static BufferedImage returnBuffImage(String path) 
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
        return "";
    }

    /**
     * Unique label to be used in future caching system
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
        int width = 0;
        if(image != null)
            width = image.getWidth();

        return width;
    }

    public int getHeight()
    {
        int height = 0;
        if(image != null)
            height = image.getHeight();

        return height;
    }

    public Graphics2D createGraphics()
    {
        return image.createGraphics();
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
