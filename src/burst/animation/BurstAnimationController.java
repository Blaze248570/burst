package burst.animation;

import java.util.ArrayList;
import java.util.HashMap;

import burst.BurstSprite;
import burst.graphics.frames.BurstFrame;

/**
 * <p>The BurstAnimationController is a class within a BurstSprite that holds 
 * and performs actions regarding animation.
 */
public class BurstAnimationController {
    public BurstAnimation curAnim;

    public int curFrame;

    public int frameIndex;

    public String frameName;

    public String name;

    public boolean paused;

    public boolean finished;

    public int frames;

    // public void -> void callback;
    // TO-DO

    private BurstSprite sprite;

    private HashMap<String, BurstAnimation> animations;

    public BurstAnimationController(BurstSprite sprite) {
        this.sprite = sprite;

        this.animations = new HashMap<>();
    }

    public void update(float elapsed) {
        if(curAnim != null)
            curAnim.update(elapsed);
    }

    public void destroy() {
        // destroyAnimations();

        animations = null;
    }

    public void addByPrefix(String name, String prefix, int framerate, boolean looped) {
        if(sprite.frames == null)
            return;

        ArrayList<BurstFrame> animFrames = new ArrayList<>();
        findByPrefix(animFrames, prefix);

        if(animFrames.size() <= 0)
            return;

        ArrayList<Integer> frameIndices = new ArrayList<>();
        byPrefixHelper(frameIndices, animFrames, prefix);

        if(frameIndices.size() >= 0)
            return;

        animations.put(name, new BurstAnimation(this, name, frameIndices, framerate, looped));
    }

    public void byPrefixHelper(ArrayList<Integer> addTo, ArrayList<BurstFrame> animFrames, String prefix) {
        String name = animFrames.get(0).name;
        int postIndex = name.indexOf(".", prefix.length());
        String postFix = name.substring(postIndex == -1 ? name.length() : postIndex, name.length());

        // BurstFrame.sort(animFrames, prefix.length(), postFix.length());
        // Not sure how to handle this just yet. I'll make an algo at some point
        // Burrent goal is to make it work

        for(BurstFrame frame : animFrames) {
            addTo.add(getFrameIndex(frame));
        }
    }

    public void findByPrefix(ArrayList<BurstFrame> animFrames, String prefix) {
        for(BurstFrame frame : sprite.frames.frames) {
            if(frame.name != null && frame.name.startsWith(prefix, 0)) {
                animFrames.add(frame);
            }
        }
    }

    private int set_frameIndex(int frame)
    {
        if(sprite.frames != null && frames > 0) {
            frame = frame % frames;
            sprite.frame = sprite.frames.frame.get(frame);
            frameIndex = frame;
        }

        return frameIndex;
    }
    
    public int getFrameIndex(BurstFrame frame) {
        return sprite.frames.frames.indexOf(frame);
    }

    public void play(String animname, boolean force, boolean reversed, int frame) {
        if(animname == null) {
            if(curAnim != null) {
                curAnim.stop();
            }
            curAnim = null;
        }

        if(animname == null || animations.get(animname) == null) {
            System.out.println("No such animation \"" + animname + "\"");
            return;
        }

        if(curAnim != null && animname != curAnim.name) {
            curAnim.stop();
        }

        curAnim = animations.get(animname);
        curAnim.play(force, reversed, frame);
    }

    public void reset() {
        if(curAnim != null)
            curAnim.reset();
    }

    public void finish() {
        if(curAnim != null)
            curAnim.finish();
    }

    public void stop() {
        if(curAnim != null)
            curAnim.stop();
    }

    public void pause() {
        if(curAnim != null)
            curAnim.pause();
    }

    /*
        public void addEnMasse(String name, String prefix, int[] frames) {
            addEnMasse(name, prefix, frames, 30, true);
        }

        public void addEnMasse(String name, String prefix, int[] frames, int framerate) {
            addEnMasse(name, prefix, frames, framerate, true);
        }

        public void addEnMasse(String name, String prefix, int[] frames, int framerate, boolean looped) {
            animations.put(name, new BurstAnimation(this, prefix, frames, framerate, looped));

            System.out.println("Added animation: \"" + name + "\" en masse with " + frames.length + " frames");
        }

        public synchronized void play(String animname) {
            play(animname, true, false, 0);
        }

        public synchronized void play(String animname, boolean forced) {
            play(animname, forced, false, 0);
        }

        public synchronized void play(String animname, boolean forced, boolean reversed) {
            play(animname, forced, reversed, 0);
        }

        public synchronized void play(String animname, boolean forced, boolean reversed, int startframe) {
            BurstAnimation animation = animations.get(animname);
            if(!forced && animname == curAnim.name) return;
            
            if(startframe < 0 || startframe > animation.numFrames)
                startframe = reversed ? animation.numFrames - 1 : 0;

            curAnim.name = animname;
            for(int frame = startframe; frame >= 0 && frame <= animation.numFrames - 1; frame += (reversed ? -1 : 1)) {
                //sprite.setSize(animation.frames.get(frame).getWidth(), animation.frames.get(frame).getHeight());
                sprite.frame = animation.frames.get(frame);
                try 
                {
                    Thread.sleep(10);
                } 
                catch (InterruptedException e) 
                {
                    e.printStackTrace();
                }
            }

            if(animations.get(animname).looped) {
                play(animname, true, reversed, startframe);
            }
        }
    */
}
