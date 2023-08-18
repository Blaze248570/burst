package burst.animation;

public class JBurstAnimation 
{
    /**
     * The name of the animation.
     */
    public String name;

    /**
     * The speed in frames per second of the animation.
     */
    public float frameRate;

    /**
     * The current frame of the animation.
     */
    public int curFrame;

    /**
     * The total number of frames in this animation.
     */
    public int numFrames;

    /**
     * The delay between frames in milliseconds
     */
    public float delay;

    /**
     * Whether the animation has finsihed playing or not.
     */
    public boolean finished = true;

    /**
     * Whether the animation should update or not.
     */
    public boolean paused;

    /**
     * Whether this animation should loop or not.
     */
    public boolean looped;

    /**
     * Whether this animation plays backwards or not.
     */
    public boolean reversed;

    /**
     * A list of frames stored as integer indices.
     */
    public int[] frames;

    /**
     * Internal, used to time each frame of animation.
     */
    private float _frameTimer;

    /**
     * Internal, reference to owner controller.
     */
    private JBurstAnimationController _controller;

    public JBurstAnimation(JBurstAnimationController controller, String name, int[] frames, int frameRate, boolean looped) 
    {
        this._controller = controller;
        this.name = name;

        this.frameRate = frameRate;
        this.frames = frames;
        this.numFrames = frames.length;
        this.frameRate = frameRate;
        this.looped = looped;
    }

    /**
     * Starts playback of this animation.
     * 
     * @param force     Whether to force this animation to restart.
     * @param reversed  Whether to play animation backwards or not.
     * @param frame     The frame index to start from.
     */
    public void play(boolean force, boolean reversed, int frame)
    {
        if(!force && !finished && this.reversed == reversed) 
        {
            paused = false;
            finished = false;
            return;
        }

        delay = 1000 / frameRate;

        this.reversed = reversed;
        paused = false;
        _frameTimer = 0;
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
    }

    /**
     * Restarts the animation.
     */
    public void restart() 
    {
        play(true, reversed, 0);
    }

    /**
     * Stops the animation.
     */
    public void stop() 
    {
        finished = true;
        paused = true;
    }

    /**
     * Resets the animation.
     */
    public void reset() 
    {
        stop();
        curFrame = reversed ? (numFrames - 1) : 0;
    }

    /**
     * Finishes the animation.
     */
    public void finish() 
    {
        stop();
        curFrame = reversed ? 0 : (numFrames - 1);
    }

    /**
     * Pauses the current animation.
     */
    public void pause()
    {
        paused = true;
    }

    /**
     * Resumes the current animation.
     */
    public void resume() 
    {
        paused = false;
    }

    public void update(float elapsed) 
    {
        if(delay == 0 || finished || paused) return;

        _frameTimer += elapsed;
        while(_frameTimer > delay && !finished) 
        {
            _frameTimer -= delay;
			if (reversed)
            {
                curFrame--;
                if(curFrame > 0)
                {
                    if (looped && curFrame <= 0)
                        _controller._sprite.setFrame(_controller._sprite.frames.get(frames[numFrames - 1]));
                    else
                        _controller._sprite.setFrame(_controller._sprite.frames.get(frames[curFrame]));
                }

			}
            else 
            {
                curFrame++;
                if(curFrame < numFrames)
                {
                    if (looped && curFrame >= numFrames - 1)
                        _controller._sprite.setFrame(_controller._sprite.frames.get(frames[0]));
                    else
                        _controller._sprite.setFrame(_controller._sprite.frames.get(frames[curFrame]));
                }
			}

            finished = (reversed ? curFrame < 0 : curFrame >= numFrames);

            if(finished && looped)
            {
                curFrame = (reversed ? numFrames - 1 : 0);
                finished = false;
            }
        }
    }

    @Override
    public String toString()
    {
        return "BurstAnimation ~ {name: " + name + ", framerate: " + frameRate + ", looped: " + looped + ", reversed: " + reversed + "}";
    }
}
