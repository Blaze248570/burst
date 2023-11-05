package com.github.jbb248.jburst;

import java.util.ArrayList;

import com.github.jbb248.jburst.util.JBurstDestroyUtil;

/**
 * Core of JBurst package and manager of all JBurstBasics.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 * and <a href="https://api.haxeflixel.com/flixel/FlxGame.html">FlxGame</a>
 */
public class JBurst
{
    /**
     * Whether or not ALL JBurst objects can update
     */
    public static boolean active = false;

    /**
     * All currently active JBurstBasic objects are held within this ArrayList.
     */
    protected static ArrayList<JBurstBasic> members = new ArrayList<>();

    private static long _startTime = 0;

    private static double _total = 0.0;

    private static int _frameRate = 60;

    private static double _elapsed = 0.0;

    private static double _step = 1000.0 / _frameRate;

    private static double _accumulator = _step;

    /**
     * Independent thread running sprite update system
     */
    private static Thread _burstThread = new Thread("Burst") 
    {
        @Override
        public void run()
        {
            _startTime = System.currentTimeMillis();
            active = true;

            while(!isInterrupted())
            {
                double ticks = getTicks();
                _elapsed = ticks - _total;
                _total = ticks;

                _accumulator += _elapsed;

                while(_accumulator >= _step) 
                {
                    step();
                    _accumulator -= _step;
                }
            }

            active = false;
        }
    };

    private static void step()
    {
        if(!active || members.size() == 0) return;

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null)
            {
                if(basic.exists && basic.active)
                    basic.update(0.016);
                
                basic.repaint();
            }
        }
    }

    private static long getTicks()
    {
        return System.currentTimeMillis() - _startTime;
    }

    /**
     * Returns how often {@code JBurst} should update its members, in frames per second
     */
    public static int getFrameRate()
    {
        return _frameRate;
    }

    /**
     * Sets how often {@code JBurst} should update its members, in frames per second.
     * <p> The default frame rate is 60 fps.
     * <p>
     * <i>If {@code framerate} is less than 1, this call will be ignored.</i>
     */
    public static void setFrameRate(int frameRate)
    {
        _frameRate = frameRate < 1 ? _frameRate : frameRate;
        _step = frameRate > 0 ? 1000.0 / frameRate : 0;
    }

    /**
     * "Kills" {@code JBurst}, causing it to cease updating
     * 
     * @see #revive()
     */
    public static void kill()
    {
        active = false;
    }

    /**
     * "Revives" {@code JBurst}, causing it to continue updating
     * 
     * @see #kill()
     */
    public static void revive()
    {
        active = true;
    }

    /**
     * Resets {@code JBurst} and destroys all of its current members
     * <p>
     * <i>
     *  Warning: This will <strong>destroy</strong> every single object managed 
     *  by JBurst, rendering them completely useless.
     *  To simply disable JBurst, use {@code kill()}.
     * </i>
     * 
     * @see #kill()
     */
    public static void reset()
    {
        JBurstDestroyUtil.destroyArrayList(members);
        System.gc();

        _startTime = System.currentTimeMillis();
    }

    static {
        JBurst._burstThread.start();
    }
}