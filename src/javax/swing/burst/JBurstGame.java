package javax.swing.burst;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import javax.swing.JFrame;

/**
 * An extended version of the {@code javax.swing.JFrame}
 * that allows basic animation animation through Burst objects.
 * <p>
 * Because it extends JFrame, JComponents such as JPanel, 
 * JLabel, JButton, etc. can still be added to a JBurstGame.
 */
@SuppressWarnings("rawtypes")
public class JBurstGame extends JFrame
{
    /**
     * Time in milliseconds since program began
     */
    private long _total;

    private final Class _initialState;

    private final Instant _startTime;

    private final Thread burstThread = new Thread()
    {
        @Override
        public void run()
        {
            while(true)
            {
                elapsed = getTotal() - _total;
                _total = getTotal();

                update();
            }
        }
    };

    private JBurstState _state;
    protected JBurstState _requestedState;

    private long elapsed;

    /**
     * Creates a new JBurst window object.
     * 
     * @param width         The width of the newly created window
     * @param height        The height of the newly created window
     * @param initialState  The class of the state this JBurst object should begin with
     */
    public JBurstGame(int width, int height, Class initialState) 
    {
        super();

        this._initialState = initialState;

        /*
         * Format the window.
         */
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX = (int)(resolution.getWidth() / 2 - width / 2);
        int windowY = (int)(resolution.getHeight() / 2 - height / 2);
        setLocation(windowX, windowY);

        /*
         * 
         */
        JBurst.init(this, new Dimension(width, height));
        _startTime = Instant.now();

        _total = getTotal();

        reset();
        switchState();

        setSize(JBurst.size);

        setTitle("JBurst Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the program ends upon termination
        setLayout(null);
        add(JBurst.defaultCam);

        burstThread.start();
        setVisible(true);
    }

    @SuppressWarnings("unchecked")
    private void reset()
    {
        try
        {
            Class<JBurstState> initState = (Class<JBurstState>) _initialState;

            _requestedState = initState.getConstructor().newInstance();
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { }
        catch(ClassCastException e)
        {
            System.out.println("The class given to JBurst must be or extend JBurstState.");
            System.exit(0);
        }
    }

    private void switchState()
    {
        // JBurst.cameras.reset(); // For when I get around to cameras

        // if(_state != null) state.destroy();

        _state = JBurst.state = _requestedState;

        _state.create();
    }

    private void update()
    {
        // if(!_state.active || !_state.exists) return;

        if(_state != _requestedState) switchState();

        JBurst.elapsed = elapsed;

        _state.update();

        JBurst.defaultCam.update();
    }

    private long getTotal()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }
}