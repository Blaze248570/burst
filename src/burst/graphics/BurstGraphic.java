package burst.graphics;

import java.awt.image.BufferedImage;

public class BurstGraphic 
{
    public static BurstGraphic fromBuffImage(BufferedImage source, String key) 
    {
        return new BurstGraphic(key, source);
    }

    public static BurstGraphic fromBuffImage(String source, String key) 
    {
        return new BurstGraphic(key, returnBuffImage(source));
    }

    public static BufferedImage returnBuffImage(String path) 
    {
        BufferedImage img = null;
        try 
        {
            img = javax.imageio.ImageIO.read(new java.io.File(path));
        } 
        catch(java.io.IOException exception) 
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

    public BurstGraphic(String key, BufferedImage data) 
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
