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
    public final int ID = idEnumerator++;

    static int idEnumerator = 0;

    public boolean active = true;
    public boolean visible = true;
    public boolean alive = true;
    public boolean exists = true;

    public JBurstBasic() { }

    public void kill() 
    {
        alive = false;
        exists = false;
    }

    public void revive() 
    {
        alive = true;
        exists = true;
    }

    public void update(float elapsed) { }

    public void draw() { }
}
