package burst;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/**
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst
{
    public ArrayList<JBurstBasic> members;

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

    private final Instant _startTime = Instant.now();

    private Thread burstThread = new Thread() 
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
     * Appends an element to the end of {@code members}.
     * <p>
     * <i>Used internally to match containers</i>
     * 
     * @param element   the element to be added to this group
     * @return  whether or not the element was successfully added
     */
    public boolean add(JBurstBasic element)
    {
        element.burst = this;
        return members.add(element);
    }

    /**
     * Inserts an element at {@code index}.
     * <p>
     * <i>Used internally to match containers</i>
     * 
     * @param element   the element to be added to this group
     * @return  whether or not the element was successfully added
     */
    public void add(int index, JBurstBasic element)
    {
        element.burst = this;
        members.add(index, element);
    }

    /**
     * Replaces the element at {@code index} with {@code element}.
     * <p>
     * If {@code index} is less than zero or exceeds the length of {@code members}, 
     * the element will be appended to the end.
     * <p>
     * <i>Used internally to match containers</i>
     * 
     * @param index     the index of the element to be replaced
     * @param element   the element to be set at {@code index}
     * @return  the element that was replaced
     */
    public JBurstBasic set(int index, JBurstBasic element)
    {
        element.burst = this;
        return members.set(index, element);
    }

    /**
     * Removes an element from {@code members}, leaving the open space as {@code null}
     * <p>
     * <i>Used internally to match containers</i>
     * 
     * @param element   the element to be removed
     * @return  whether or not the JBurst contained {@code element}
     */
    public boolean remove(JBurstBasic element)
    {
        element.burst = null;
        return members.remove(element);
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
     * Stops the JBurst from further updates, 
     * destroys all of its current members, and kills its thread.
     * <p>
     * <i>
     *  Warning: This will render every single object added to this JBurst completely useless.
     *  Use {@code clear()} to prevent the objects from destruction.
     *  To simply disable this JBurst, use {@code kill()}
     * </i>
     * 
     * @see {@link #kill()}
     */
    public void destroy()
    {
        if(burstThread == null)
        {
            burstThread.interrupt();
            burstThread = null;
        }

        members = burst.util.JBurstDestroyUtil.destroyArrayList(members);

        System.gc();
    }
}