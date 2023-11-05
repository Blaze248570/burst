package com.github.jbb248.jburst.animation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import com.github.jbb248.jburst.JBurstSprite;
import com.github.jbb248.jburst.graphics.JBurstGraphic;
import com.github.jbb248.jburst.graphics.frames.JBurstAtlasFrames;
import com.github.jbb248.jburst.graphics.frames.JBurstFrame;
import com.github.jbb248.jburst.graphics.frames.JBurstFramesCollection;
import com.github.jbb248.jburst.util.JBurstDestroyUtil.IBurstDestroyable;
import com.github.jbb248.jburst.util.function.TriConsumer;

/**
 * A class that manages and performs animation operations.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/animation/FlxAnimationController.html">FlxAnimationController</a>
 */
public class JBurstAnimationController implements IBurstDestroyable
{
    /**
     * The currently playing animation, which may be {@code null}.
     */
    public JBurstAnimation curAnim;

    /**
     * The frame index of the current animation.
     */
    public int frameIndex = -1;

    public TriConsumer<String, Integer, Integer> callback;

    public Consumer<String> finishedCallBack;

    /**
     * Internal, reference to owner sprite.
     */
    protected JBurstSprite _sprite;

    /**
     * Internal, storage of animations added to this sprite.
     */
    private HashMap<String, JBurstAnimation> _animations;

    public JBurstAnimationController(JBurstSprite sprite) 
    {
        this._sprite = sprite;
        this._animations = new HashMap<>();
    }

    public void update(double elapsed) 
    {
        if(curAnim != null)
        {
            curAnim.update(elapsed);
        }
    }

    /**
     * Adds an animation to the parent sprite.
     * <i>To be used after {@code loadAnimatedSprite}.</i>
     * <p>
     * The {@code frames} object required will be which indices to use to animate with.
     * <p>
     * For example, providing 
     * <p> <code>animation.add("Dance", new int[] {0, 1, 2, 4, 6}, 30, true)</code>
     * <p> will create an animation named "Dance" using the first, second, third, fifth, and seventh frames.
     * <p> <i> If an animation titled {@code name} already exists, it will be overwritten</i>
     * 
     * @param name      the title for the new animation
     * @param frames    which frames the new animation displays
     * 
     * @see JBurstSprite#loadAnimatedGraphic(String, int, int) JBurstSprite.loadAnimatedGraphic()
     */
    public void add(String name, int[] frames)
    {
        add(name, frames, 30);
    }

    /**
     * Adds an animation to the parent sprite.
     * <i>To be used after {@code loadAnimatedSprite}.</i>
     * <p>
     * The {@code frames} object required will be which indices to use to animate with.
     * <p>
     * For example, providing 
     * <p> <code>animation.add("Dance", new int[] {0, 1, 2, 4, 6}, 30, true)</code>
     * <p> will create an animation named "Dance" using the first, second, third, fifth, and seventh frames.
     * <p> <i> If an animation titled {@code name} already exists, it will be overwritten</i>
     * 
     * @param name      the title for the new animation
     * @param frames    which frames the new animation displays
     * @param framerate the speed this animation should play at in frames per second
     * 
     * @see JBurstSprite#loadAnimatedGraphic(String, int, int) JBurstSprite.loadAnimatedGraphic()
     */
    public void add(String name, int[] frames, int framerate)
    {
        add(name, frames, framerate, true);
    }

    /**
     * Adds an animation to the parent sprite.
     * <i>To be used after {@code loadAnimatedSprite}.</i>
     * <p>
     * The {@code frames} object required will be which indices to use to animate with.
     * <p>
     * For example, providing 
     * <p> <code>animation.add("Dance", new int[] {0, 1, 2, 3, 4}, 30, true)</code>
     * <p> will create an animation named "Dance" using the first five frames.
     * <p> <i> If an animation titled {@code name} already exists, it will be overwritten</i>
     * 
     * @param name      the title for the new animation
     * @param frames    which frames the new animation displays
     * @param framerate the speed this animation should play at in frames per second
     * @param looped    whether or not the new animation should replay when finished
     * 
     * @see JBurstSprite#loadAnimatedGraphic(String, int, int) JBurstSprite.loadAnimatedGraphic()
     */
    public void add(String name, int[] frames, int framerate, boolean looped)
    {
        add(name, frames, framerate, looped, false, false);
    }

    /**
     * Adds an animation to the parent sprite.
     * <i>To be used after {@code loadAnimatedSprite}.</i>
     * <p>
     * The {@code frames} object required will be which indices to use to animate with.
     * <p>
     * For example, providing 
     * <p> <code>animation.add("Dance", new int[] {0, 1, 2, 3, 4}, 30, true)</code>
     * <p> will create an animation named "Dance" using the first five frames.
     * <p> <i> If an animation titled {@code name} already exists, it will be overwritten</i>
     * 
     * @param name      the title for the new animation
     * @param frames    which frames the new animation displays
     * @param framerate the speed this animation should play at in frames per second
     * @param looped    whether or not the new animation should replay when finished
     * @param flipX         whether or not this animation should render backwards
     * @param flipY         whether or not this animation should render upside-down
     * 
     * @see JBurstSprite#loadAnimatedGraphic(String, int, int) JBurstSprite.loadAnimatedGraphic()
     */
    public void add(String name, int[] frames, int framerate, boolean looped, boolean flipX, boolean flipY)
    {
        _animations.put(name, new JBurstAnimation(this, name, frames, framerate, looped, flipX, flipY));
    }

    /**
     * Adds an animation to the parent sprite using a key from their animation data
     * 
     * @param name      what to name the animation
     * @param prefix    name of the animation on the animation file
     * 
     * @see JBurstSprite#setFrames(JBurstFramesCollection) JBurstSprite.setFrames()
     * @see JBurstAtlasFrames#fromSparrow(JBurstGraphic, String) JBurstAtlasFrames.fromSparrow()
     * @see JBurstAtlasFrames#fromJsonPacker(JBurstGraphic, String) JBurstAtlasFrames.fromJsonPacker()
     */
    public void addByPrefix(String name, String prefix)
    {
        addByPrefix(name, prefix, 30);
    }

    /**
     * Adds an animation to the parent sprite using a key from their animation data
     * 
     * @param name          what to name the animation
     * @param prefix        name of the animation on the animation file
     * @param framerate     how fat or slow this animation should play
     * 
     * @see JBurstSprite#setFrames(JBurstFramesCollection) JBurstSprite.setFrames()
     * @see JBurstAtlasFrames#fromSparrow(JBurstGraphic, String) JBurstAtlasFrames.fromSparrow()
     * @see JBurstAtlasFrames#fromJsonPacker(JBurstGraphic, String) JBurstAtlasFrames.fromJsonPacker()
     */
    public void addByPrefix(String name, String prefix, int framerate)
    {
        addByPrefix(name, prefix, framerate, true);
    }

    /**
     * Adds an animation to the parent sprite using a key from their animation data
     * 
     * @param name          what to name the animation
     * @param prefix        name of the animation on the animation file
     * @param framerate     how fat or slow this animation should play
     * @param looped        whether or not this animation should play again once it is finished
     * 
     * @see JBurstSprite#setFrames(JBurstFramesCollection) JBurstSprite.setFrames()
     * @see JBurstAtlasFrames#fromSparrow(JBurstGraphic, String) JBurstAtlasFrames.fromSparrow()
     * @see JBurstAtlasFrames#fromJsonPacker(JBurstGraphic, String) JBurstAtlasFrames.fromJsonPacker()
     */
    public void addByPrefix(String name, String prefix, int framerate, boolean looped)
    {
        addByPrefix(name, prefix, framerate, looped, false, false);
    }

    /**
     * Adds an animation to the parent sprite using a key from their animation data
     * 
     * @param name          what to name the animation
     * @param prefix        name of the animation on the animation file
     * @param framerate     how fat or slow this animation should play
     * @param looped        whether or not this animation should play again once it is finished
     * @param flipX         whether or not this animation should render backwards
     * @param flipY         whether or not this animation should render upside-down
     * 
     * @see JBurstSprite#setFrames(JBurstFramesCollection) JBurstSprite.setFrames()
     * @see JBurstAtlasFrames#fromSparrow(JBurstGraphic, String) JBurstAtlasFrames.fromSparrow()
     * @see JBurstAtlasFrames#fromJsonPacker(JBurstGraphic, String) JBurstAtlasFrames.fromJsonPacker()
     */
    public void addByPrefix(String name, String prefix, int framerate, boolean looped, boolean flipX, boolean flipY)
    {
        if(_sprite.getFrames() == null)
            return;

        ArrayList<JBurstFrame> animFrames = new ArrayList<>();
        findByPrefix(animFrames, prefix);

        if(animFrames.size() <= 0)
            return;

        ArrayList<Integer> frameIndices = new ArrayList<>(animFrames.size());
        byPrefixHelper(frameIndices, animFrames, prefix);

        if(frameIndices.size() <= 0)
            return;

        int[] arrIndices = new int[frameIndices.size()];
        for(int i = 0; i < frameIndices.size(); i++) arrIndices[i] = frameIndices.get(i);

        JBurstAnimation anim = new JBurstAnimation(this, name, arrIndices, framerate, looped, flipX, flipY);
        _animations.put(name, anim);
    }

    private void byPrefixHelper(ArrayList<Integer> addTo, ArrayList<JBurstFrame> animFrames, String prefix) 
    {
        for(JBurstFrame frame : animFrames) 
        {
            addTo.add(getFrameIndex(frame));
        }
    }

    private void findByPrefix(ArrayList<JBurstFrame> animFrames, String prefix) 
    {
        JBurstFramesCollection frames = _sprite.getFrames();
        for(JBurstFrame frame : frames.frames) 
        {
            if(frame.name != null && frame.name.startsWith(prefix, 0)) 
            {
                animFrames.add(frame);
            }
        }
    }
    
    private int getFrameIndex(JBurstFrame frame) 
    {
        return _sprite.getFrames().frames.indexOf(frame);
    }

    /**
     * Plays the animation under the name {@code animname}.
     * 
     * @param animname what animation to play
     */
    public void play(String animname)
    {
        play(animname, true);
    }

    /**
     * Plays the animation under the name {@code animname}.
     * 
     * @param animname  what animation to play
     * @param force     whether to force the animation to restart or not
     */
    public void play(String animname, boolean force)
    {
        play(animname, force, false);
    }

    /**
     * Plays the animation under the name {@code animname}.
     * 
     * @param animname  what animation to play
     * @param force     whether to force the animation to restart or not
     * @param reversed  whether or not the animation should play backwards
     */
    public void play(String animname, boolean force, boolean reversed)
    {
        play(animname, force, reversed, 0);
    }

    /**
     * Plays the animation under the name {@code animname}.
     * 
     * @param animname  what animation to play
     * @param force     whether to force the animation to restart or not
     * @param reversed  whether or not the animation should play backwards
     * @param frame     what frame to begin the animation at
     */
    public void play(String animname, boolean force, boolean reversed, int frame) 
    {
        if(animname == null) 
        {
            if(curAnim != null) 
            {
                curAnim.stop();
            }
            curAnim = null;
        }

        if(animname == null || _animations.get(animname) == null) 
        {
            System.out.println("No such animation \"" + animname + "\"");
            return;
        }

        if(curAnim != null && animname != curAnim.name)
            curAnim.stop();

        curAnim = _animations.get(animname);
        curAnim.play(force, reversed, frame);
    }

    /**
     * Resets the current animation.
     */
    public void reset() 
    {
        if(curAnim != null)
            curAnim.reset();
    }

    /**
     * Finishes the current animation.
     */
    public void finish() 
    {
        if(curAnim != null)
            curAnim.finish();
    }

    /**
     * Stops the current animation.
     */
    public void stop() 
    {
        if(curAnim != null)
            curAnim.stop();
    }

    /**
     * Pauses the current animation.
     */
    public void pause() 
    {
        if(curAnim != null)
            curAnim.pause();
    }

    /**
     * Returns a list of all the animations' names.
     * <p><i>This does not return the animation objects themselves.</i>
     */
    public ArrayList<String> getNamesList()
    {
        ArrayList<String> list = new ArrayList<>();
        for(JBurstAnimation anim : _animations.values())
        {
            list.add(anim.name);
        }

        return list;
    }

    /**
     * Returns a list of all the animations.
     * <p><i>This does not return the animation names.</i>
     */
    public ArrayList<JBurstAnimation> getAnimationsList()
    {
        ArrayList<JBurstAnimation> list = new ArrayList<>();
        for(JBurstAnimation anim : _animations.values())
        {
            list.add(anim);
        }

        return list;
    }

    /**
     * Clears all animations added to the parent sprite.
     */
    public void clearAnimations()
    {
        if(_animations != null)
        {
            JBurstAnimation anim;
            for(String key : _animations.keySet())
            {
                anim = _animations.get(key);
                if(anim != null)
                    anim.destroy();
            }
        }

        _animations = new HashMap<>();
        curAnim = null;
        frameIndex = -1;
    }

    protected void fireCallback()
    {
        if(callback != null)
        {
            String name = curAnim != null ? curAnim.name : null;
            int number = curAnim != null ? curAnim.curFrame : frameIndex;

            callback.accept(name, number, frameIndex);
        }
    }

    protected void fireFinishedCallback(String name)
    {
        if(finishedCallBack != null)
        {
            finishedCallBack.accept(name);
        }
    }

    public void setFrameIndex(int frame)
    {
        frameIndex = frame;

        JBurstFramesCollection frames = _sprite.getFrames();

        if(frames != null && getNumFrames() > 0)
        {
            _sprite.setFrame(frames.frames.get(frameIndex));

            fireCallback();
        }
    }

    public int getNumFrames()
    {
        return _sprite.getNumFrames();
    }

    @Override
    public void destroy()
    { 
        clearAnimations();
        _animations = null;
        callback = null;
        _sprite = null;
    }

    @Override
    public String toString()
    {
        return String.format("%s[curanim=%s,frameindex=%d]", getClass().getName(), curAnim.toString(), frameIndex);
    }
}
