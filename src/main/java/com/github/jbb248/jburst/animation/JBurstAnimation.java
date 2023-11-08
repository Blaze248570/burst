package com.github.jbb248.jburst.animation;

import com.github.jbb248.jburst.util.JBurstDestroyUtil.IBurstDestroyable;

/**
 * Structure used to store animation data.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/animation/FlxAnimation.html">FlxAnimation</a>
 */
public class JBurstAnimation implements IBurstDestroyable
{
    /**
     * The name of the animation.
     */
    public String name;

    /**
     * The current index in regards to the tilesheet.
     */
    public int curIndex;

    /**
     * The current frame of the animation.
     */
    public int curFrame;

    /**
     * The total number of frames in this animation.
     */
    public int numFrames;

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
     * Whether or not this animation should render backwards
     */
    public boolean flipX = false;

    /**
     * Whether or not this animation should render upside-down
     */
    public boolean flipY = false;

    /**
     * The speed in frames per second of the animation.
     */
    private int _frameRate;

    /**
     * The delay between frames in milliseconds
     */
    private double _frameDelay;

    /**
     * Internal, used to time each frame of animation.
     */
    private double _frameTimer;

    /**
     * Internal, reference to owner controller.
     */
    private JBurstAnimationController _controller;

    public JBurstAnimation(JBurstAnimationController controller, String name, int[] frames, int frameRate, boolean looped, boolean flipX, boolean flipY) 
    {
        this._controller = controller;
        this.name = name;
        this.frames = frames;
        this.numFrames = Math.min(frames.length, _controller._sprite.getNumFrames());
        this.looped = looped;
        this.flipX = flipX;
        this.flipY = flipY;
        setFrameRate(frameRate);
    }

    /**
     * Starts playback of this animation.
     * 
     * @param force     whether to force this animation to restart.
     * @param reversed  whether to play animation backwards or not.
     * @param frame     the frame index to start from.
     */
    public void play(boolean force, boolean reversed, int frame)
    {
        if(!force && !finished && this.reversed == reversed) 
        {
            paused = false;
            return;
        }

        this.reversed = reversed;
        paused = false;
        _frameTimer = 0;
        finished = _frameDelay == 0;

        int maxFrameIndex = numFrames - 1;
        if(frame < 0) 
            setCurFrame((int)(Math.random() * maxFrameIndex));
        else 
        {
            if(frame > maxFrameIndex)
                frame = maxFrameIndex;
            if(reversed)
                frame = (maxFrameIndex - frame);
                
            setCurFrame(frame);
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

    public void update(double elapsed) 
    {
        if(_frameDelay == 0 || finished || paused) return;

        _frameTimer += elapsed;
        while(_frameTimer > _frameDelay && !finished) 
        {
            _frameTimer -= _frameDelay;
            int curFrame = this.curFrame;
			if (reversed)
                curFrame--;
            else 
                curFrame++;

            finished = (reversed ? curFrame < 0 : curFrame >= numFrames);

            if(finished && looped)
            {
                setCurFrame(reversed ? numFrames - 1 : 0);
                finished = false;
            }
            else
                setCurFrame(curFrame);
        }
    }

    public void setCurFrame(int frame)
    {
        int maxFrameIndex = numFrames - 1;
        int tempFrame = reversed ? maxFrameIndex - frame : frame;

        if(tempFrame >= 0)
        {
            if(!looped && frame > maxFrameIndex)
            {
                finished = true;
                curFrame = reversed ? 0 : maxFrameIndex;
            }
            else
                curFrame = frame;
        }

        setCurIndex(frames[curFrame]);

        if(finished && _controller != null)
        {
            _controller.fireFinishedCallback(name);
        }
    }

    public void setCurIndex(int value)
    {
        curIndex = value;
        
        _controller.setFrameIndex(curIndex);
    }

    public int getFrameRate()
    {
        return _frameRate;
    }

    public void setFrameRate(int frameRate)
    {
        _frameRate = frameRate;
        _frameDelay = frameRate > 0 ? 1.0 / frameRate : 0;
    }

    @Override
    public void destroy() 
    { 
        frames = null;
        name = null;
        _controller = null;
    }

    @Override
    public String toString()
    {
        return String.format("%s[name=\"%s\",framerate=%d,looped=%b,reversed=%b]", 
            getClass().getName(), name, _frameRate, looped, reversed);
    }
}
