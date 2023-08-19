package javax.swing.burst;

import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A layer and grouping manager intended to better implemented in the future.
 */
public class JBurstCamera extends JPanel
{
    public JBurst parent;

     /**
     * A list of all objects added to this JBurstCamera.
     */
    public ArrayList<JBurstSprite> members;

    /**
     * Constructs a new JBurstCamera object.
     * <p> There's no need to make anymore of these, though.
     * Not yet anyway...
     * 
     * @param parent The JBurst object to be managing this JBurstCamera
     */
    public JBurstCamera(JBurst parent)
    {
        this.parent = parent;

        setLayout(null);
        // setBackground(java.awt.Color.GREEN);

        setBounds(0, 0, parent.getWidth(), parent.getHeight());
        members = new ArrayList<>();
    }

    /**
     * Adds an object to the list of sprites being drawn.
     * 
     * @param sprite    The sprite to be added.
     * @return          The JBurstBasic that was added.
     */
    public JBurstSprite add(JBurstSprite sprite)
    {
        members.add(sprite);
        add((JComponent) sprite);

        return sprite;
    }

    public void update(float elapsed)
    {
        for(JBurstSprite sprite : members)
        {
            if(sprite != null && sprite.exists)
                sprite.repaint();
        }
    }
}
