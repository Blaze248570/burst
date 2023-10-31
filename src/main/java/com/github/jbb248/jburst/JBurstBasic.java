package com.github.jbb248.jburst;

import com.github.jbb248.jburst.util.JBurstDestroyUtil.IBurstDestroyable;
import javax.swing.JComponent;

/**
 * The JBurstBasic class is sort of just the simple JBurstSprite.
 * <p>
 * It can be used to create an object that is persistently updated, 
 * but not quite as heavy as a JBurstSprite.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxBasic.html">FlxBasic</a>
 */
public class JBurstBasic extends JComponent implements IBurstDestroyable
{
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
     * Creates a new JBurstBasic
     */
    public JBurstBasic() 
    { 
        if(JBurst.BURST != null)
            JBurst.BURST.members.add(this);
    }

    /**
     * "Kills" the object, causing it to cease updating <i>and</i> drawing
     */
    public void kill() 
    {
        alive = false;
        exists = false;
    }

    /**
     * "Revives" the object, causing it continue updating <i>and</i> drawing
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
        if(JBurst.BURST != null)
            JBurst.BURST.members.remove(this);
    }

    @Override
    public String toString()
    {
        return String.format("%s[exists=%b,alive=%b,active=%b]", getClass().getName(), exists, alive, active);
    }
}
