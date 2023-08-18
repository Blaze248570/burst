package javax.swing.burst;

import java.time.Duration;
import java.time.Instant;

/**
 * An extended version of the {@code javax.swing.JFrame}
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

    /**
     * The default camera that objects are sent to.
     */
    public JBurstCamera defaultCam;

    private Instant startTime;

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
        setLocation(525, 200);
        setDefaultCloseOperation(3);

        startTime = Instant.now();
        
        defaultCam = new JBurstCamera(this);
        add(defaultCam);
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

        defaultCam.update(elapsed);
    }

    private long getTime()
    {
        return Duration.between(startTime, Instant.now()).toMillis();
    }
}
