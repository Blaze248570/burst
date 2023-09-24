package burst;

import burst.util.JBurstDestroyUtil.IBurstDestroyable;
import javax.swing.JComponent;

/**
 * The JBurstBasic class is sort of the building block for JBurstSprite.
 * <p>
 * This class could be used to create an object that is persistently updated, 
 * but not quite as heavy as JBurstSprite.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxBasic.html">FlxBasic</a>
 */
public class JBurstBasic extends JComponent implements IBurstDestroyable
{
    public static JBurst defaultBurst = new JBurst();
    protected JBurst burst = defaultBurst;
    
    static int idEnumerator = 0;
    public final int ID = idEnumerator++;

    /**
     * Whether or not this object should update
     */
    public boolean active = true;

    /**
     * JBurst does not use this. It's for you!
     * <p>
     * It is toggled off from {@code kill()} and on from {@code revive()}.
     */
    public boolean alive = true;

    /**
     * Whether or not this object should update <i>and</i> draw
     */
    public boolean exists = true;

    /**
     * Whether or not this object is drawn
     */
    public boolean visible = true;

    /**
     * Creates a new JBurstBasic
     */
    public JBurstBasic() 
    { 
        if(burst != null)
            burst.add(this);
    }

    /**
     * "Kills" the object, causing it to cease updating <i>and</i> drawing.
     */
    public void kill() 
    {
        alive = false;
        exists = false;
    }

    /**
     * "Revives" the object, causing it continue updating <i>and</i> drawing.
     */
    public void revive() 
    {
        alive = true;
        exists = true;
    }

    public void update(int elapsed) { }

    /**
     * Removes this object from JBurst's list of members
     */
    public void destroy() 
    {
        if(burst != null)
            burst.remove(this);
    }
}
