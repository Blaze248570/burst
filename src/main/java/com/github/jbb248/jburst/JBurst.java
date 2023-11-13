package com.github.jbb248.jburst;

import java.util.ArrayList;

import com.github.jbb248.jburst.util.JBurstDestroyUtil;

/**
 * The JBurst class is the core of the JBurst package. It manages every non-destroyed JBurstBasic.
 * It also contains static methods to alter frame rate and pause all JBurstBasics universally.
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

    /**
     * The measured starting time used to calculate the elapsed time in milliseconds
     */
    private static long _startTime = 0;

    /**
     * The total time in milliseconds since {@code _startTime} was decided
     */
    private static long _total = 0;

    /**
     * The rate at which JBurst updates, in frames per second
     */
    private static int _frameRate = 60;

    /**
     * The amount of time between frames, in milliseconds
     */
    private static double _stepMS = 1000.0 / _frameRate;

    /**
     * The amount of time between frames, in seconds
     */
    private static double _step = _stepMS / 1000.0;

    private static double _accumulator = _stepMS;

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
                long ticks = System.currentTimeMillis() - _startTime;
                long _elapsed = ticks - _total;
                _total = ticks;

                _accumulator += _elapsed;

                while(_accumulator >= _stepMS) 
                {
                    update();
                    _accumulator -= _stepMS;
                }
            }

            active = false;
        }
    };

    private static void update()
    {
        if(!active || members.size() == 0) return;

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null)
            {
                if(basic.exists && basic.active)
                    basic.update(_step);

                basic.repaint();
            }
        }
    }

    /**
     * Returns how often {@code JBurst} should update its members, in frames per second.
     * <p> 
     * <i>This does not affect draw speed. JBurst's frame rate is simply how often {@code update()} is called.</i>
     */
    public static int getFrameRate()
    {
        return _frameRate;
    }

    /**
     * Sets how often {@code JBurst} should update its members, in frames per second.
     * The default frame rate is 60 fps.
     * <p>
     * <i>If {@code framerate} is less than 1, this call will be ignored.</i>
     * <p>
     * <i>This does not affect draw speed. JBurst's frame rate is simply how often {@code update()} is called.</i>
     */
    public static void setFrameRate(int frameRate)
    {
        _frameRate = frameRate < 1 ? _frameRate : frameRate;
        _step = (_stepMS = frameRate > 0 ? 1000.0 / frameRate : 0) / 1000.0;
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