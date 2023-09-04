package javax.swing.burst;

import java.awt.Component;
import java.util.ArrayList;
import javax.swing.burst.group.JBurstGroup;

public class JBurstState extends JBurstGroup<JBurstBasic>
{
    public ArrayList<JBurstBasic> members;

    public JBurst parent;

    public JBurstState()
    {
        members = new ArrayList<>();
    }

    public void create() { }

    /**
     * Adds the given Component to the default camera, <strong>not the JBurst object</strong>.
     * 
     * @param comp  The component to be added
     * 
     * @return  The component that was added
     */
    public Component add(Component comp)
    {
        if(comp instanceof JBurstBasic)
            return add((JBurstBasic) comp);
        
        return JBurst.defaultCam.add(comp);
    }

    /**
     * Adds the given basic to the default camera, <strong>not the JBurst object</strong>.
     * 
     * @param basic The basic to be added
     * 
     * @return  The basic that was added
     */
    @Override
    public JBurstBasic add(JBurstBasic object)
    {
        if(object instanceof JBurstSprite)
            JBurst.defaultCam.add((JBurstSprite) object);

        return super.add(object);
    }

    public void startOutro(Runnable onOutroComplete)
    {
        onOutroComplete.run();
    }
}
