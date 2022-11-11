package burst.animation;

import java.util.HashMap;

import burst.BurstSprite;

/**
 * <p>The BurstAnimationController is a class within a BurstSprite that holds 
 * and performs actions regarding animation.
 * 
 * <p>It extends {@code HashMap}, using {@code<String, ArrayList<BurstAnimation>>}
 */

public class BurstAnimationController extends HashMap<String, BurstAnimation> {
    BurstSprite sprite;
    String curAnim;

    public BurstAnimationController(BurstSprite sprite) {
        this.sprite = sprite;
    }



    public void addEnMasse(String name, String prefix, int[] frames) {
        put(name, new BurstAnimation(this, prefix, frames, 24, true));

        System.out.println("Added animation: \"" + name + "\" en masse with " + frames.length + " frames");
    }

    public void addEnMasse(String name, String prefix, int[] frames, int framerate) {
        put(name, new BurstAnimation(this, prefix, frames, framerate, true));

        System.out.println("Added animation: \"" + name + "\" en masse with " + frames.length + " frames");
    }

    public void addEnMasse(String name, String prefix, int[] frames, int framerate, boolean looped) {
        put(name, new BurstAnimation(this, prefix, frames, framerate, looped));

        System.out.println("Added animation: \"" + name + "\" en masse with " + frames.length + " frames");
    }


    /**
     * Adds animation to the sprite using an xml file
     * @param name The name of the animation
     * @param animname The name of the animation on the xml file
     * @param framerate How fast the animation should play
     * @param looped If the animation should repeat once finished
     */
    public void addViaSparrow(String name, String animname, int framerate, boolean looped) {
        put(name, new BurstAnimation(this, animname, framerate, looped));
    }


    @Deprecated
    public void addByIndices() {

    }


    public synchronized void play(String animname) {
        BurstAnimation animation = get(animname);
        if(animname == curAnim) return;

        curAnim = animname;
        for(int frame = 0; frame >= 0 && frame <= animation.numFrames - 1; frame++) {
            //sprite.setSize(animation.frames.get(frame).getWidth(), animation.frames.get(frame).getHeight());
            sprite.frame = animation.frames.get(frame);
            try {
                Thread.sleep(1000 / animation.framerate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(get(animname).looped) {
            play(animname, true);
        }
    }

    public synchronized void play(String animname, boolean forced) {
        BurstAnimation animation = get(animname);
        if(!forced && animname == curAnim) return;

        curAnim = animname;
        for(int frame = 0; frame >= 0 && frame <= animation.numFrames - 1; frame++) {
            //sprite.setSize(animation.frames.get(frame).getWidth(), animation.frames.get(frame).getHeight());
            sprite.frame = animation.frames.get(frame);
            try {
                Thread.sleep(1000 / animation.framerate);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(get(animname).looped) {
            play(animname, true);
        }
    }

    public synchronized void play(String animname, boolean forced, boolean reversed) {
        BurstAnimation animation = get(animname);
        if(!forced && animname == curAnim) return;

        curAnim = animname;
        for(int frame = reversed ? animation.numFrames - 1 : 0; frame >= 0 && frame <= animation.numFrames - 1; frame += (reversed ? -1 : 1)) {
            //sprite.setSize(animation.frames.get(frame).getWidth(), animation.frames.get(frame).getHeight());
            sprite.frame = animation.frames.get(frame);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(get(animname).looped) {
            play(animname, true, reversed);
        }
    }

    public synchronized void play(String animname, boolean forced, boolean reversed, int startframe) {
        BurstAnimation animation = get(animname);
        if(!forced && animname == curAnim) return;
        
        if(startframe < 0 || startframe > animation.numFrames)
            startframe = reversed ? animation.numFrames - 1 : 0;

        curAnim = animname;
        for(int frame = startframe; frame >= 0 && frame <= animation.numFrames - 1; frame += (reversed ? -1 : 1)) {
            //sprite.setSize(animation.frames.get(frame).getWidth(), animation.frames.get(frame).getHeight());
            sprite.frame = animation.frames.get(frame);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(get(animname).looped) {
            play(animname, true, reversed, startframe);
        }
    }
}
