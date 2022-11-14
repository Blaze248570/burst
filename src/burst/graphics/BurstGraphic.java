package burst.graphics;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class BurstGraphic extends ImageIcon {
    public static BurstGraphic fromBuffImage(BufferedImage source, String key) {
        return new BurstGraphic(key, source);
    }

    public static BurstGraphic fromBuffImage(String source, String key) {
        return new BurstGraphic(key, returnBuffImage(source));
    }

    public static BufferedImage returnBuffImage(String path) {
        try {
            return ImageIO.read(new FileInputStream(new File(path)));
        } catch(IOException e) {
            System.out.println("Image not found: " + path);
            if(!path.endsWith(".png") && !path.endsWith(".gif"))
                System.out.println("[You may need to specify the file type {.png or .gif preferably}]");
            return null;
        }
    }

    public String key;

    public BufferedImage image;

    public Graphics data;

    public BurstGraphic(String key, BufferedImage image) {
        super();
        this.key = key;
        this.image = image;

        System.out.println("Made Graphic");
        data = image.getGraphics();
    }
}
