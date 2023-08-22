package javax.swing.burst;

import java.awt.Component;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 * An extended version of the {@code javax.swing.JFrame}
 * that allows basic animation animation through Burst objects.
 * <p>
 * It functions identically to JFrame, so objects like JPanel, 
 * JLabel, JButton, etc. can still be added to Burst.
 */
public class JBurst extends JFrame
{
    /**
     * Time in milliseconds since program began
     */
    public double total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public double elapsed;

    /**
     * The default camera that objects are sent to.
     */
    public JBurstCamera defaultCam;

    public ArrayList<JBurstBasic> members;

    private final Instant _startTime;

    /**
     * Creates a new JBurst window object.
     * To make the window visible, call {@code setVisible(true)}.
     * 
     * @param frameWidth    The width of the newly created window.
     * @param frameHeight   The height of the newly created window.
     */
    public JBurst(int frameWidth, int frameHeight) 
    {
        super();

        setLayout(null);
        setTitle("Burst");
        setSize(frameWidth, frameHeight);
        setLocation(100, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        _startTime = Instant.now();
        
        members = new ArrayList<>();
        defaultCam = new JBurstCamera(this);
        addImpl(defaultCam, null, -1);

        // setVisible(true);
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
        
        return defaultCam.add(comp);
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
            defaultCam.add((JBurstSprite) basic);

        members.add(basic);

        return basic;
    }

    /**
     * Used to update Burst objects added this JBurst.
     * <p>
     * For now, this should be placed within a while loop
     * so that it is perpetually called.
     * <p>
     * Bad practice most likely, 
     * but it'll do for the time being.
     */
    public void update()
    {
        elapsed = getTime() - total;
        total = getTime();

        for(JBurstBasic basic : members)
        {
            if(basic != null && basic.exists && basic.active)
                basic.update(elapsed);
        }

        defaultCam.update(elapsed);
    }

    private long getTime()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }
}
