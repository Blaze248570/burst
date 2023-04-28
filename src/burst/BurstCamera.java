package burst;

/**
 * A layering class intended to better implemented in the future
 */
public class BurstCamera extends java.awt.Container
{
    public Burst parent;

    public BurstCamera(Burst parent)
    {
        this.parent = parent;

        setBounds(0, 0, parent.getWidth(), parent.getHeight());
    }
}
