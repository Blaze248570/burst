package burst;

/**
 * A layer and grouping manager intended to better implemented in the future.
 */
public class JBurstCamera extends java.awt.Container
{
    public JBurst parent;

    /**
     * Constructs a new JBurstCamera object.
     * <p>
     * There's no need to make anymore of these, though.
     * Not yet anyway...
     * 
     * @param parent The JBurst object to be managing this JBurstCamera
     */
    public JBurstCamera(JBurst parent)
    {
        this.parent = parent;

        setBounds(0, 0, parent.getWidth(), parent.getHeight());
    }
}
