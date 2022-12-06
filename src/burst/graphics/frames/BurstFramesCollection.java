package burst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;

import burst.graphics.BurstGraphic;

public class BurstFramesCollection  {

    public ArrayList<BurstFrame> frames = new ArrayList<>();
    public HashMap<String, BurstFrame> framesHash = new HashMap<>();

    public BurstGraphic parent;

    public BurstFrameCollectionType type;

    public BurstFramesCollection(BurstGraphic parent, BurstFrameCollectionType type) {
        this.parent = parent;
        this.type = type;

        frames.clear();

        if(parent != null)
            parent.addFramesCollection(this);
    }

    public BurstFrame addAtlasFrame(Rectangle frame, Point sourceSize, Point offset, String name) {
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        BurstFrame texFrame = new BurstFrame(parent);
        texFrame.name = name;
        texFrame.sourceSize.setLocation(sourceSize.x, sourceSize.y);
        texFrame.offset.setLocation(offset.x, offset.y);
        texFrame.frame = checkFrame(frame, name);

        return pushFrame(texFrame);
    }

    /**
     * Ensures the frame isn't outside the images boundaries
     * 
     * @param frame Frame area to check
     * @param name  Frame name for debuggin info
     * @return      Checked and trimmed frame rectangle
     */
    private Rectangle checkFrame(Rectangle frame, String name) {
        int x = frame.x;
        if(x > parent.image.getWidth())
            x -= (x - parent.image.getWidth());

        int y = frame.y;
        if(y > parent.image.getHeight())
            y -= (y - parent.image.getHeight());

        int right = (frame.x + frame.width);
        if(right > parent.image.getWidth())
            right -= (right - parent.image.getWidth());

        int bottom = (frame.y + frame.height);
        if(bottom > parent.image.getHeight())
            bottom -= (bottom - parent.image.getHeight());

        frame.setFrame(x, y, right - x, bottom - y);

        if(frame.width <= 0 || frame.height <= 0)
            System.out.println("The frame " + name + " has incorrect data and results in an image with the size of (0, 0)");

        return frame;
    }

    public BurstFrame pushFrame(BurstFrame frameObj) {
        String name = frameObj.name;
        if(framesHash.containsKey(name))
            return framesHash.get(name);

        frames.add(frameObj);

        framesHash.put(name, frameObj);

        return frameObj;
    }

    /**
     * Just for organization
     */
    public enum BurstFrameCollectionType {
        ATLAS;
    }

    java.util.Random num;
}