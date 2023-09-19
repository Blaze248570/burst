package burst;

import java.time.Duration;
import java.time.Instant;

import burst.group.JBurstGroup;

/**
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst
{
    /**
     * Managing group of every JBurstBasic held by JBurst
     */
    public static final JBurstGroup<JBurstBasic> members = new JBurstGroup<>();

    /**
     * Whether or not ALL JBurstBasics should update
     */
    public static boolean active = false;

    /**
     * Time in milliseconds since program began
     */
    private static long _total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public static int elapsed;

    private static final Instant _startTime = Instant.now();

    protected static boolean initialized = false;

    private static Thread burstThread;

    protected static void init()
    {
        if(initialized) return;

        _total = getTotal();

        burstThread = new Thread() 
        {
            @Override
            public void run()
            {
                while(!isInterrupted())
                {
                    elapsed = Math.toIntExact(getTotal() - _total);
                    _total = getTotal();

                    update();
                }
            }
        };
        burstThread.start();

        active = true;
        initialized = true;
    }

    private static void update()
    {
        if(!active || members.size() == 0) return;

        members.update(elapsed);
        members.repaint();
    }

    private static long getTotal()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }

    /**
     * Stops the JBurst from further updates, 
     * removes all of its current members, and kills its thread
     */
    public static void destroy()
    {
        if(burstThread == null) return;
        
        burstThread.interrupt();
        burstThread = null;

        active = false;
        initialized = false;
    }
}