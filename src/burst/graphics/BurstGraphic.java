package burst.graphics;

import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import burst.graphics.frames.BurstFramesCollection;
import burst.graphics.frames.BurstFramesCollection.BurstFrameCollectionType;

public class BurstGraphic {
    public static BurstGraphic fromBuffImage(BufferedImage source, String key) {
        return new BurstGraphic(key, source);
    }

    public static BurstGraphic fromBuffImage(String source, String key) {
        return new BurstGraphic(key, returnBuffImage(source));
    }

    public static BufferedImage returnBuffImage(String path) {
        try {
            return javax.imageio.ImageIO.read(new FileInputStream(new File(path)));
        } catch(IOException e) {
            System.out.println("Image not found: " + path);
            if(!path.endsWith(".png") && !path.endsWith(".gif"))
                System.out.println("[You may need to specify the file type {.png or .gif preferably}]");
            return null;
        }
    }

    public String key;

    public BufferedImage data;

    public int width = 0;

    public int height = 0;

    public HashMap<BurstFrameCollectionType, ArrayList<BurstFramesCollection>> frameCollections = new HashMap<>();

    public BurstGraphic(String key, BufferedImage data) {
        this.key = key;
        this.data = data;
    }

    public void addFramesCollection(BurstFramesCollection collection) {
        if(collection.type == null)
            return;

        ArrayList<BurstFramesCollection> collections = getFramesCollection(collection.type);
        collections.add(collection);
    }

    public ArrayList<BurstFramesCollection> getFramesCollection(BurstFrameCollectionType type) {
        ArrayList<BurstFramesCollection> collections = frameCollections.get(type);
        if (collections == null)
		{
			collections = new ArrayList<>();
			frameCollections.put(type, collections);
		}

        return collections;
    }
}
