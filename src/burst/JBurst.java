package burst;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

/**
 * An extended version of the <code>javax.swing.JFrame</code> 
 * that allows basic animation animation through Burst objects.
 * <p>
 * It functions identically to JFrame, so objects like JPanel, 
 * JLabel, JButton, etc. can still be added to Burst.
 */
public class JBurst extends javax.swing.JFrame 
{
    /**
     * Time in milliseconds since program began
     */
    public float total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public float elapsed;
    private Instant startTime;

    /**
     * The default camera that objects are sent to.
     */
    public JBurstCamera defaultCam;

    /**
     * A list of all objects added to this JBurst.
     */
    public ArrayList<JBurstBasic> members;

    /**
     * Creates a new JBurst window object.
     * To make the window visible, call <code>setVisible(true)</code>
     * 
     * @param frameWidth  The width of the newly created window.
     * @param frameHeight The height of the newly created window.
     */
    public JBurst(int frameWidth, int frameHeight) 
    {
        super();

        setLayout(null);
        setTitle("Burst");
        setSize(frameWidth, frameHeight);
        setLocation(525, 200);
        setDefaultCloseOperation(3);

        startTime = Instant.now();
        
        defaultCam = new JBurstCamera(this);
        add(defaultCam);

        members = new ArrayList<>();
    }

    /**
     * Used to update Burst objects added this JBurst.
     * 
     * For now, this should be placed within a while loop
     * so that it is perpetually called.
     * <p>
     * Bad practice probably, 
     * but it'll do for my silly little school project.
     */
    public void update()
    {
        elapsed = getTime() - total;
        total = getTime();

        for(JBurstBasic basic : members) 
        {
            if(basic != null && basic.exists)
            {
                basic.update(elapsed);
            }
        }

        defaultCam.repaint();
    }

    /**
     * Adds an object to the list of objects being updated.
     * 
     * @param basic The object to be added.
     * @return The JBurstBasic that was added.
     */
    public JBurstBasic add(JBurstBasic basic)
    {
        members.add(basic);
        defaultCam.add(basic);

        return basic;
    }

    private long getTime()
    {
        return Duration.between(startTime, Instant.now()).toMillis();
    }
}
