package com.github.jbb248.jburst;

import javax.swing.JComponent;

import com.github.jbb248.jburst.util.JBurstDestroyUtil.IBurstDestroyable;

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
     * Whether or not this object should update <i>and</i> draw
     */
    public boolean exists = false;

    /**
     * JBurst does not use this. It's for you!
     * <p>
     * It is toggled off from {@code kill()} and on from {@code revive()}.
     */
    public boolean alive = true;

    /**
     * Creates a new JBurstBasic
     */
    public JBurstBasic() { }

    /**
     * Appends this JBurstBasic to JBurst's list of objects to update,
     * allowing it to begin updating and drawing
     */
    public void start()
    {
        revive(); 
    }

    /**
     * "Revives" the object, causing it continue updating <i>and</i> drawing
     * 
     * @deprecated  in favor of {@code start()}
     * @see #start()
     */
    @Deprecated
    public void revive() 
    {
        alive = true;
        exists = true;

        if(!JBurst.members.contains(this))
            JBurst.members.add(this);
    }

    /**
     * Removes this JBurstBasic to JBurst's list of objects to update,
     * stopping it from begin updating and drawing
     */
    public void stop()
    {
        kill();
    }

    /**
     * "Kills" the object, causing it to cease updating <i>and</i> drawing
     * 
     * @deprecated  in favor of {@code stop()}
     * @see #stop()
     */
    public void kill() 
    {
        alive = false;
        exists = false;

        JBurst.members.remove(this);
        repaint(); // Ensure that the sprite is cleared
    }

    public void update(double elapsed) { }

    /**
     * Removes this object from JBurst's list of members
     */
    public void destroy() 
    {
        kill();
    }

    @Override
    public String toString()
    {
        return String.format("%s[exists=%b,alive=%b,active=%b]", getClass().getName(), exists, alive, active);
    }
}
