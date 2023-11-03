package com.github.jbb248.jburst;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import com.github.jbb248.jburst.util.JBurstDestroyUtil;

/**
 * Core of JBurst package and manager of all JBurstBasics.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst
{
    /**
     * Whether or not ALL of this JBurst's objects should update
     */
    public static boolean active = false;

    /**
     * All currently active JBurstBasic objects are held within this ArrayList.
     * <p> 
     * You shouldn't need to add JBurstBasics manually as they automatically
     * add themselves when they are instantiated.
     */
    protected static ArrayList<JBurstBasic> members = new ArrayList<>();

    /**
     * Time in milliseconds since program began
     */
    private static long _total;

    private static Instant _startTime = Instant.now();

    private static int _framerate = 60;

    /**
     * Independent thread running sprite update system
     */
    private static Thread _burstThread = new Thread("Burst-Manager") 
    {
        @Override
        public void run()
        {
            active = true;

            while(!isInterrupted())
            {
                int delay = 1000 / _framerate;
                while(delay - Math.toIntExact(getTotal() - _total) >= 0);

                _total = getTotal();

                update();
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
                    basic.update(1000 / _framerate);
                basic.repaint();
            }
        }
    }

    private static long getTotal()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }

    /**
     * Returns how often {@code JBurst} should update its members, in frames per second
     */
    public static int getFramerate()
    {
        return _framerate;
    }

    /**
     * Sets how often {@code JBurst} should update its members, in frames per second.
     * <p> The default frame rate is 60 fps.
     * <p>
     * <i>If {@code framerate} is less than 1, this call will be ignored.</i>
     */
    public static void setFramerate(int framerate)
    {
        _framerate = framerate < 1 ? _framerate : framerate;
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

        _startTime = Instant.now();
    }

    static {
        JBurst._burstThread.start();
    }
}