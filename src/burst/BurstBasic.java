package burst;

public class BurstBasic extends javax.swing.JComponent 
{
    public final int ID = idEnumerator++;

    static int idEnumerator = 0;

    public boolean active = true;
    public boolean visible = true;
    public boolean alive = true;
    public boolean exists = true;

    public BurstBasic() {}

    public void destroy() 
    {
        exists = false;
    }

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

    public void update(float elapsed) {}

    public void draw() {}
}
