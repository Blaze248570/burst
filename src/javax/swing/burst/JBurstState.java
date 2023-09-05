package javax.swing.burst;

import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.burst.group.JBurstGroup;

public class JBurstState extends JBurstGroup<JBurstBasic>
{
    public ArrayList<JComponent> components = new ArrayList<>();

    public void create() { }

    /**
     * 
     * 
     * @param comp  The component to be added
     * 
     * @return  The component that was added
     */
    public JComponent add(JComponent comp)
    {
        if(comp instanceof JBurstBasic)
            return add((JBurstBasic) comp);
        
        components.add(comp);
        JBurst.defaultCam.add(comp);
        return comp;
    }

    /**
     * 
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

    @Override 
    public void destroy()
    {
        super.destroy();

        for(int i = 0; i < components.size(); i++)
        {
            JComponent comp = components.get(i);
            JBurst.defaultCam.remove(comp);
            components.remove(comp);

            i--;
        }

        components = null;
    }
}
