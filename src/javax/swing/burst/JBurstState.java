package javax.swing.burst;

import java.awt.Component;
import java.util.ArrayList;

public class JBurstState 
{
    public ArrayList<JBurstBasic> members;

    public JBurst parent;

    public JBurstState()
    {
        members = new ArrayList<>();
    }

    public void create() { }

    public void update()
    {
        for(JBurstBasic basic : members)
        {
            if(basic != null && basic.exists && basic.active)
                basic.update(JBurst.elapsed);
        }
    }

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
    public JBurstBasic add(JBurstBasic basic)
    {
        if(basic instanceof JBurstSprite)
            JBurst.defaultCam.add((JBurstSprite) basic);

        members.add(basic);

        return basic;
    }

    public void startOutro(Runnable onOutroComplete)
    {
        onOutroComplete.run();
    }
}
