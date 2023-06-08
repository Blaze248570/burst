package burst.graphics;

import java.awt.image.BufferedImage;

public class JBurstGraphic 
{
    /**
     * Returns a JBurstGraphic using the BufferedImage, <code>source</code>.
     * 
     * @param source Image to be used in returned JBurstGraphic.
     * @param key Unique title for this JBurstGraphic.
     */
    public static JBurstGraphic fromBuffImage(BufferedImage source, String key) 
    {
        return new JBurstGraphic(key, source);
    }

    /**
     * Returns a JBurstGraphic using the image file specified at <code>source</code>.
     * 
     * @param source Path of image asset to be used in returned JBurstGraphic.
     * @param key Unique title for this JBurstGraphic.
     */
    public static JBurstGraphic fromFile(String source, String key) 
    {
        return new JBurstGraphic(key, returnBuffImage(source));
    }

    public static BufferedImage returnBuffImage(String path) 
    {
        BufferedImage img = null;
        try 
        {
            img = javax.imageio.ImageIO.read(new java.io.File(path));
        } 
        catch(java.io.IOException e) 
        {
            System.out.print("Image not found: " + path);
            if(!path.endsWith(".png") && !path.endsWith(".gif"))
            {
                System.out.println(" (You may need to specify the file type [.png or .gif preferably])");
            }
        }

        return img;
    }

    public String key;
    public int width = 0;
    public int height = 0;
    public BufferedImage data;

    public JBurstGraphic(String key, BufferedImage data) 
    {
        this.key = key;
        this.data = data;
        
        this.width = data.getWidth();
        this.height = data.getHeight();
    }

    @Override public String toString()
    {
        return "BurstGraphic ~ {key: " + this.key + ", width: " + this.width + ", height: " + this.height + "}";
    }
}
