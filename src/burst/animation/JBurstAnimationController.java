package burst.animation;

import java.util.ArrayList;
// import java.util.function.Consumer;
import java.util.HashMap;

import burst.JBurstSprite;
import burst.graphics.frames.JBurstFrame;

/**
 * The BurstAnimationController is a class within a JBurstSprite that holds 
 * and performs actions regarding animation.
 */
public class JBurstAnimationController 
{
    public JBurstSprite sprite;
    public JBurstAnimation curAnim;
    public HashMap<String, JBurstAnimation> animations;

    public int frames;
    public int curFrame;

    public String frameName;
    public String name;

    public boolean paused;
    public boolean finished;

    // To-Do: Figure out Lambda junk for Java
    // public Consumer<Void> callback;

    public JBurstAnimationController(JBurstSprite sprite) 
    {
        this.sprite = sprite;

        this.animations = new HashMap<>();
    }

    public void update(float elapsed) 
    {
        if(curAnim != null)
        {
            curAnim.update(elapsed);
        }
    }

    /**
     * Adds an animation to the parent sprite.
     * To be used after <code>loadAnimatedSprite</code>.
     * <p>
     * The <code>frames</code> object required will be which indices to use to animate with.
     * <p>
     * For example, providing <p><code>animation.add("Dance", new int[] {0, 1, 2, 4, 6}, 30, true)</code></p>
     * will create an animation named "Dance" using the first, second, third, fifth, 
     * and seventh frames that does loop updating at 30 fps.
     */
    public void add(String name, int[] frames, int framerate, boolean looped)
    {
        JBurstAnimation anim = new JBurstAnimation(this, name, frames, framerate, looped);

        animations.put(name, anim);
    }

    /**
     * Adds an animation to the parent sprite.
     * To be used after <code>loadFrames()</code>.
     * 
     * @param name What to name the animation.
     * @param prefix Name of the animation on the animation file.
     * @param framerate How fat or slow this animation should play.
     * @param looped Whether or not this animation should play again once it is finished.
     */
    public void addByPrefix(String name, String prefix, int framerate, boolean looped) 
    {
        if(sprite.frames == null)
            return;

        ArrayList<JBurstFrame> animFrames = new ArrayList<>();
        findByPrefix(animFrames, prefix);

        if(animFrames.size() <= 0)
            return;

        ArrayList<Integer> frameIndices = new ArrayList<>();
        byPrefixHelper(frameIndices, animFrames, prefix);

        if(frameIndices.size() <= 0)
            return;

        int[] arrIndices = new int[frameIndices.size()];
        for(int i = 0; i < frameIndices.size(); i++) arrIndices[i] = frameIndices.get(i);

        JBurstAnimation anim = new JBurstAnimation(this, name, arrIndices, framerate, looped);
        animations.put(name, anim);
    }

    private void byPrefixHelper(ArrayList<Integer> addTo, ArrayList<JBurstFrame> animFrames, String prefix) 
    {
        // String name = animFrames.get(0).name;
        // int postIndex = name.indexOf(".", prefix.length());
        // String postFix = name.substring(postIndex == -1 ? name.length() : postIndex, name.length());

        // BurstFrame.sort(animFrames, prefix.length(), postFix.length());
        // Not sure how to handle this just yet. I'll make an algo at some point
        // Current goal is to make it work

        for(JBurstFrame frame : animFrames) 
        {
            addTo.add(getFrameIndex(frame));
        }
    }

    private void findByPrefix(ArrayList<JBurstFrame> animFrames, String prefix) 
    {
        for(JBurstFrame frame : sprite.frames) 
        {
            if(frame.name != null && frame.name.startsWith(prefix, 0)) 
            {
                animFrames.add(frame);
            }
        }
    }
    
    private int getFrameIndex(JBurstFrame frame) 
    {
        return sprite.frames.indexOf(frame);
    }

    /**
     * Plays the animation under the name <code>animname</code>.
     */
    public void play(String animname)
    {
        play(animname, true);
    }

    /**
     * Plays the animation under the name <code>animname</code>.
     * 
     * @param animname What animation to play.
     * @param force If the current animation is the same as the one provided, 
     *              it will not be replayed unless <code>force</code> is true.
     */
    public void play(String animname, boolean force)
    {
        play(animname, force, false);
    }

    /**
     * Plays the animation under the name <code>animname</code>.
     * 
     * @param animname What animation to play.
     * @param force If the current animation is the same as the one provided, 
     *              it will not be replayed unless <code>force</code> is true.
     * @param reversed Whether or not the animation should play backwards.
     */
    public void play(String animname, boolean force, boolean reversed)
    {
        play(animname, force, reversed, 0);
    }

    /**
     * Plays the animation under the name <code>animname</code>.
     * 
     * @param animname What animation to play.
     * @param force If the current animation is the same as the one provided, 
     *              it will not be replayed unless <code>force</code> is true.
     * @param reversed Whether or not the animation should play backwards.
     * @param frame What frame to begin the animation at.
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

        if(animname == null || animations.get(animname) == null) 
        {
            System.out.println("No such animation \"" + animname + "\"");
            return;
        }

        if(curAnim != null && animname != curAnim.name)
            curAnim.stop();

        curAnim = animations.get(animname);
        curAnim.play(force, reversed, frame);
    }

    /**
     * Resets the current animation.
     */
    public void reset() {
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
     * Returns a list of all the animations <i>names</i>.
     * <p>
     * This does not return the animation objects themselves!
     */
    public ArrayList<String> getNamesList()
    {
        ArrayList<String> list = new ArrayList<>();
        for(JBurstAnimation anim : animations.values())
        {
            list.add(anim.name);
        }

        return list;
    }

    /**
     * Returns a list of all the animations.
     * <p>
     * This does not return the animation objects' names!
     */
    public ArrayList<JBurstAnimation> getAnimationsList()
    {
        ArrayList<JBurstAnimation> list = new ArrayList<>();
        for(JBurstAnimation anim : animations.values())
        {
            list.add(anim);
        }

        return list;
    }

    /**
     * Clears all animations added to the parent sprite.
     */
    public void clearFrames()
    {
        this.animations.clear();
        this.curAnim = null;
        this.frames = 0;
        this.curFrame = 0;
    }
}
