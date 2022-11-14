package burst.animation;

import java.awt.Rectangle;

import java.util.ArrayList;

import burst.XMLData;
import burst.XMLSparrowParser;
import burst.graphics.BurstGraphic;

public class BurstAnimation {
    public BurstAnimationController parent;
    public int framerate = 30;
    public int numFrames = 0;
    public boolean looped = false;
    public ArrayList<BurstGraphic> frames = new ArrayList<>();

    public BurstAnimation(BurstAnimationController parent, String prefix, int framerate, boolean looped) {
        this.parent = parent;
        this.framerate = framerate;
        this.looped = looped;

        ArrayList<XMLData> data = XMLSparrowParser.parseXML("parent.sprite.imagePath");
        BurstGraphic toBeSliced = parent.sprite.frame;
        for(XMLData x : data) {
            if(x.name.contains(prefix)) {
                Boolean trimmed = (x.frameX != 0);
                
                Rectangle rect = new Rectangle(x.x, x.y, x.width, x.height);
                Rectangle size = (trimmed ? 
                    new Rectangle(x.frameX, x.frameY, x.frameWidth, x.frameHeight) :
                    new Rectangle(0, 0, rect.width, rect.height)
                );

                frames.add(BurstGraphic.fromBuffImage(toBeSliced.image.getSubimage(rect.x, rect.y, size.width + size.x, size.height + size.y), prefix));

                numFrames++;
            }
        }
    }

    public BurstAnimation(BurstAnimationController parent, String prefix, int[] frames, int framerate, boolean looped) {
        this.framerate = framerate;
        this.looped = looped;
        
        for(int i : frames) {
            String frameNum = i + "";
            while(frameNum.length() < 4) frameNum = "0" + frameNum;
            this.frames.add(BurstGraphic.fromBuffImage(prefix + frameNum, prefix));

            numFrames++;
        }
    }
}
