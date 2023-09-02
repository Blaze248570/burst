package javax.swing.burst;

import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A layer and grouping manager intended to better implemented in the future.
 */
public class JBurstCamera extends JPanel
{
    /**
     * A list of all objects added to this JBurstCamera.
     */
    public final ArrayList<JBurstSprite> members;

    /**
     * Constructs a new JBurstCamera object.
     * <p> 
     * There's no need to make anymore of these, though.
     * Not yet anyway...
     * 
     * @param parent The JBurst object to be managing this JBurstCamera
     */
    public JBurstCamera()
    {
        this.members = new ArrayList<>();

        setLayout(null);
        setSize(JBurst.size);
    }

    /**
     * Adds an object to the list of sprites being drawn.
     * 
     * @param sprite    The sprite to be added.
     * 
     * @return  The JBurstBasic that was added.
     */
    public JBurstSprite add(JBurstSprite sprite)
    {
        members.add(sprite);
        add((JComponent) sprite);

        return sprite;
    }

    public void update()
    {
        for(JBurstSprite sprite : members)
        {
            if(sprite != null)
                sprite.repaint();
        }
    }
}
