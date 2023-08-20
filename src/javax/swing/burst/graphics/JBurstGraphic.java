package javax.swing.burst.graphics;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class JBurstGraphic 
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
        BufferedImage img = null;
        try 
        {
            img = ImageIO.read(new File(path));
        } 
        catch(IOException e) 
        {
            System.out.print("Image not found: " + path);
        }

        return img;
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

    public JBurstGraphic(String key, BufferedImage image) 
    {
        this.key = key;
        this.image = image;
    }

    public Graphics2D getPixels()
    {
        Graphics2D pixels = null;
        if(image != null)
            pixels = image.createGraphics();

        return pixels;
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

    @Override public String toString()
    {
        return "BurstGraphic ~ {key: " + key + ", width: " + getWidth() + ", height: " + getHeight() + "}";
    }
}
