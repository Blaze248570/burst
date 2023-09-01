package javax.swing.burst;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
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
@SuppressWarnings("rawtypes")
public class JBurst extends JFrame
{
    public static int WINDOW_WIDTH;
    public static int WINDOW_HEIGHT;

    private JBurstState currentState;

    /**
     * Time in milliseconds since program began
     */
    public double total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public double elapsed;

    private final Class _initialState;

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
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public JBurst(int frameWidth, int frameHeight, Class initialState) 
    {
        super();

        this._initialState = initialState;

        initState();

        /*
         * Format the window.
         */
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX = (int)(resolution.getWidth() / 2 - frameWidth / 2);
        int windowY = (int)(resolution.getHeight() / 2 - frameHeight / 2);
        setLocation(windowX, windowY); // Centers the window on the screen


        WINDOW_WIDTH = frameWidth;
        WINDOW_HEIGHT = frameHeight;

        setSize(frameWidth, frameHeight);
        setTitle("JBurst Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the program ends upon termination
        
        /*
         * Instantiate variables and setup engine.
         */
        members = new ArrayList<>();
        defaultCam = new JBurstCamera(this);
        addImpl(defaultCam, null, -1);
        setLayout(null);

        // setVisible(true);

        /*
         * Initialize update system. 
         */
        _startTime = Instant.now();

        currentState.create();
    }

    @SuppressWarnings("unchecked")
    private void initState()
    {
        try
        {
            Class<JBurstState> initState = (Class<JBurstState>) _initialState;

            currentState = initState.getConstructor().newInstance();
            currentState.parent = this;
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { }
        catch(ClassCastException e)
        {
            System.out.println("The class given to JBurst must be or extend JBurstState.");
            System.exit(0);
        }
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

        currentState.update(elapsed);
        defaultCam.update(elapsed);
    }

    private long getTime()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }
}
