package burst.animation;

import java.util.ArrayList;

public class BurstAnimation {
    public BurstAnimationController controller;

    public String name;

    public float frameRate;
    public int curFrame;
    public int numFrames;

    /**
     * The delay between frames in milliseconds
     */
    public float delay;

    public boolean finished = true;
    public boolean paused;
    public boolean looped;
    public boolean reversed;

    public ArrayList<Integer> frames;
    public float frameTimer;

    public BurstAnimation(BurstAnimationController controller, String name, ArrayList<Integer> frames, int frameRate, boolean looped) 
    {
        this.controller = controller;
        this.name = name;

        this.frameRate = frameRate;
        this.frames = frames;
        this.frameRate = frameRate;
        this.looped = looped;
    }

    public void play(boolean force, boolean reversed, int frame) 
    {
        if(!force && !finished && this.reversed == reversed) {
            paused = false;
            finished = false;
            return;
        }

        delay = 1000 / frameRate;

        this.reversed = reversed;
        paused = false;
        frameTimer = 0;
        finished = delay == 0;

        int maxFrameIndex = numFrames - 1;
        if(frame < 0) 
            curFrame = (int)(Math.random() * maxFrameIndex);
        else 
        {
            if(frame > maxFrameIndex)
                frame = maxFrameIndex;
            if(reversed)
                frame = (maxFrameIndex - frame);
            curFrame = frame;
        }

        // To-Do: Figure out Lambda junk for Java
        // parent.callback.accept(null);
    }

    public void restart() 
    {
        play(true, reversed, 0);
    }

    public void stop() 
    {
        finished = true;
        paused = true;
    }

    public void reset() 
    {
        stop();
        curFrame = reversed ? (numFrames - 1) : 0;
    }

    public void finish() 
    {
        stop();
        curFrame = reversed ? 0 : (numFrames - 1);
    }

    public void pause()
    {
        paused = true;
    }

    public void resume() 
    {
        paused = false;
    }

    public void update(float elapsed) 
    {
        if(delay == 0 || finished || paused) return;

        frameTimer += elapsed;
        while(frameTimer > delay && !finished) 
        {
            frameTimer -= delay;
			if (reversed)
            {
				if (looped && curFrame == 0)
                    controller.sprite.setFrame(controller.sprite.frames.get(numFrames - 1));
				else
                    controller.sprite.setFrame(controller.sprite.frames.get(--curFrame));

			}
            else 
            {
                /*
                    if (looped && curFrame == numFrames - 1)
                        controller.sprite.setFrame(controller.sprite.frames.get(0));
                    else
                        controller.sprite.setFrame(controller.sprite.frames.get(++curFrame));
                */
			}

            finished = (reversed ? curFrame < 0 : curFrame > frames.size());

            if(finished && looped)
            {
                curFrame = (reversed ? frames.size() - 1 : 0);
                finished = false;
            }

            // System.out.println(curFrame);
        }
    }
}
