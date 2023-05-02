package burst.animation;

import java.util.ArrayList;
// import java.util.function.Consumer;
import java.util.HashMap;

import burst.BurstSprite;
import burst.graphics.frames.BurstFrame;

/**
 * The BurstAnimationController is a class within a BurstSprite that holds 
 * and performs actions regarding animation.
 */
public class BurstAnimationController 
{
    public BurstSprite sprite;
    public BurstAnimation curAnim;
    public HashMap<String, BurstAnimation> animations;

    public int frames;
    public int curFrame;

    public String frameName;
    public String name;

    public boolean paused;
    public boolean finished;

    // To-Do: Figure out Lambda junk for Java
    // public Consumer<Void> callback;

    public BurstAnimationController(BurstSprite sprite) 
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

    public void add(String name, int[] frames)
    {
        add(name, frames, 24);
    }

    public void add(String name, int[] frames, int framerate)
    {
        add(name, frames, framerate, true);
    }

    public void add(String name, int[] frames, int framerate, boolean looped)
    {
        BurstAnimation anim = new BurstAnimation(this, name, frames, framerate, looped);

        animations.put(name, anim);
    }

    public void addByPrefix(String name, String prefix, int framerate, boolean looped) 
    {
        if(sprite.frames == null)
            return;

        ArrayList<BurstFrame> animFrames = new ArrayList<>();
        findByPrefix(animFrames, prefix);

        if(animFrames.size() <= 0)
            return;

        ArrayList<Integer> frameIndices = new ArrayList<>();
        byPrefixHelper(frameIndices, animFrames, prefix);

        if(frameIndices.size() <= 0)
            return;

        int[] arrIndices = new int[frameIndices.size()];
        for(int i = 0; i < frameIndices.size(); i++) arrIndices[i] = frameIndices.get(i);

        BurstAnimation anim = new BurstAnimation(this, name, arrIndices, framerate, looped);
        animations.put(name, anim);
    }

    public void byPrefixHelper(ArrayList<Integer> addTo, ArrayList<BurstFrame> animFrames, String prefix) 
    {
        // String name = animFrames.get(0).name;
        // int postIndex = name.indexOf(".", prefix.length());
        // String postFix = name.substring(postIndex == -1 ? name.length() : postIndex, name.length());

        // BurstFrame.sort(animFrames, prefix.length(), postFix.length());
        // Not sure how to handle this just yet. I'll make an algo at some point
        // Current goal is to make it work

        for(BurstFrame frame : animFrames) 
        {
            addTo.add(getFrameIndex(frame));
        }
    }

    public void findByPrefix(ArrayList<BurstFrame> animFrames, String prefix) 
    {
        for(BurstFrame frame : sprite.frames) 
        {
            if(frame.name != null && frame.name.startsWith(prefix, 0)) 
            {
                animFrames.add(frame);
            }
        }
    }
    
    public int getFrameIndex(BurstFrame frame) 
    {
        return sprite.frames.indexOf(frame);
    }

    public void play(String animname)
    {
        play(animname, true);
    }

    public void play(String animname, boolean force)
    {
        play(animname, force, false);
    }

    public void play(String animname, boolean force, boolean reversed)
    {
        play(animname, force, reversed, 0);
    }

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

    public void reset() {
        if(curAnim != null)
            curAnim.reset();
    }

    public void finish() 
    {
        if(curAnim != null)
            curAnim.finish();
    }

    public void stop() 
    {
        if(curAnim != null)
            curAnim.stop();
    }

    public void pause() 
    {
        if(curAnim != null)
            curAnim.pause();
    }

    public ArrayList<String> getNamesList()
    {
        ArrayList<String> list = new ArrayList<>();
        for(BurstAnimation anim : animations.values())
        {
            list.add(anim.name);
        }

        return list;
    }

    public ArrayList<BurstAnimation> getAnimationsList()
    {
        ArrayList<BurstAnimation> list = new ArrayList<>();
        for(BurstAnimation anim : animations.values())
        {
            list.add(anim);
        }

        return list;
    }

    public void clearFrames()
    {
        this.animations.clear();
        this.curAnim = null;
        this.frames = 0;
        this.curFrame = 0;
    }
}
