package javax.swing.burst;

/**
 * The JBurstBasic class is sort of the basic building block
 * for all upper JBurst objects.
 * <p>
 * If I ever advance this project, it'll be more useful,
 * but for now it's simply BurstSprite's parent.
 */
public class JBurstBasic extends javax.swing.JComponent 
{
    private static int idEnumerator = 0;
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
     * The camera this object is drawn to
     * <p>
     * Currently only a placeholder. <i>Do not assign anything to it</I>.
     */
    public JBurstCamera camera;

    public JBurstBasic() { }

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

    public void update(double elapsed) { }
}
