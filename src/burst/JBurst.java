package burst;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

import burst.util.JBurstDestroyUtil;

/**
 * Core of JBurst and manager class of all JBurstBasics.
 * <p>
 * Handles updating thread and
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst
{
    /**
     * The overarching JBurst object managing all JBurstBasics
     */
    public static final JBurst BURST = new JBurst();

    /**
     * All currently active JBurstBasic objects are held within this ArrayList.
     * <p> 
     * You shouldn't need to add JBurstBasics manually as they automatically
     * add themselves when they are instantiated.
     */
    protected ArrayList<JBurstBasic> members;

    /**
     * Whether or not ALL of this JBurst's objects should update
     */
    public boolean active = false;

    /**
     * Time in milliseconds since program began
     */
    private long _total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public int elapsed;

    private Instant _startTime = Instant.now();

    /**
     * Independent thread running sprite update system
     */
    private Thread burstThread = new Thread("Burst-Manager") 
    {
        @Override
        public void run()
        {
            active = true;

            while(!isInterrupted())
            {
                elapsed = Math.toIntExact(getTotal() - _total);
                _total = getTotal();

                update();
            }

            active = false;
        }
    };

    /**
     * Creates a new usable JBurst with its own update thread.
     * <p>
     * It's unlikely having multiple JBursts will ever be necessary,
     * but it can still be done if so desired.
     */
    public JBurst()
    {
        members = new ArrayList<>();
        burstThread.start();
    }

    private void update()
    {
        if(!active || members.size() == 0) return;

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null)
            {
                if(basic.exists && basic.active)
                    basic.update(elapsed);
                basic.repaint();
            }
        }
    }

    private long getTotal()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }

    /**
     * "Kills" this JBurst, causing it to cease updating.
     * 
     * @see {@link #revive()}
     */
    public void kill()
    {
        active = false;
    }

    /**
     * "Revives" this JBurst, causing it to continue updating.
     * 
     * @see {@link #kill()}
     */
    public void revive()
    {
        active = true;
    }

    /**
     * Stops the JBurst from further updates, and destroys all of its current members.
     * <p>
     * <i>
     *  Warning: This will render every single object added to this JBurst completely useless.
     *  To simply disable JBurst, use {@code kill()}
     * </i>
     * 
     * @see {@link #kill()}
     */
    public void reset()
    {
        JBurstDestroyUtil.destroyArrayList(members);
        System.gc();

        _startTime = Instant.now();
    }

    @Override
    public String toString()
    {
        return String.format("%s[total=%dms,elapsed=%dms,length=%d]", getClass().getName(), _total, elapsed, members.size());
    }
}