package burst.animation;

import java.util.ArrayList;

public class BurstAnimation {
    public BurstAnimationController parent;

    public String name;

    public float frameRate;
    public int curFrame;
    public int numFrames;
    public float delay;

    public boolean finished = true;
    public boolean paused;
    public boolean looped;
    public boolean reversed;

    public ArrayList<Integer> frames;
    public float frameTimer;

    public BurstAnimation(BurstAnimationController parent, String name, ArrayList<Integer> frames, int frameRate, boolean looped) {
        this.parent = parent;
        this.name = name;

        this.frameRate = frameRate;
        this.frames = frames;
        this.frameRate = frameRate;
        this.looped = looped;

        /*
            ArrayList<XMLData> data = null;
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
        */
    }

    public void play(boolean force, boolean reversed, int frame) {
        if(!force && !finished && this.reversed == reversed) {
            paused = false;
            finished = false;
            return;
        }

        this.reversed = reversed;
        paused = false;
        frameTimer = 0;
        finished = delay == 0;

        int maxFrameIndex = numFrames - 1;
        if(frame < 0) 
            curFrame = (int) (Math.random() * maxFrameIndex);
        else {
            if(frame > maxFrameIndex)
                frame = maxFrameIndex;
            if(reversed)
                frame = (maxFrameIndex - frame);
            curFrame = frame;
        }

        // Call finished callback
        // I'll need to figure out how to do that with Java
    }

    public void restart() {
        play(true, reversed, 0);
    }

    public void stop() {
        finished = true;
        paused = true;
    }

    public void reset() {
        stop();
        curFrame = reversed ? (numFrames - 1) : 0;
    }

    public void finish() {
        stop();
        curFrame = reversed ? 0 : (numFrames - 1);
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void update(float elapsed) {
        if(delay == 0 || finished || paused)
            return;

        frameTimer += elapsed;
        while(frameTimer > delay && !finished) {
            frameTimer -= delay;
			if (reversed){
				if (looped && curFrame == 0)
					curFrame = numFrames - 1;
				else
					curFrame--;
			} else {
				if (looped && curFrame == numFrames - 1)
					curFrame = 0;
				else
					curFrame++;
			}
        }
    }
}
