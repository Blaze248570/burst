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

    public boolean active = true;
    public boolean alive = true;
    public boolean exists = true;
    public boolean visible = true;

    public JBurstCamera camera;

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
}
