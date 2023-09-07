package burst;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;
import javax.swing.JFrame;
import static javax.swing.SwingUtilities.invokeLater;

/**
 * An extended version of the {@code javax.swing.JFrame}
 * that allows basic animation animation through JBurst objects.
 * <p>
 * Because it extends JFrame, JComponents such as JPanel, 
 * JLabel, JButton, etc. can still be added to a JBurstGame.
 * 
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxGame.html">FlxGame</a>
 */
public class JBurstGame extends JFrame
{
    /**
     * Time in milliseconds since program began
     */
    private long _total;

    private final Class<? extends JBurstState> _initialState;

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
    public JBurstGame(int width, int height, Class<? extends JBurstState> initialState) 
    {
        super();

        this._initialState = initialState;
        this._startTime = Instant.now();

        JBurst.init(this, new Dimension(width, height));

        /*
         * Format the window.
         */
        setSize(JBurst.size);
        
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX = (int) (resolution.getWidth() / 2 - width / 2);
        int windowY = (int) (resolution.getHeight() / 2 - height / 2);
        setLocation(windowX, windowY);

        invokeLater(() -> create());
    }

    private void create()
    {
        _total = getTotal();

        reset();
        switchState();

        setTitle("JBurst Project");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Ensures the program ends upon termination
        setLayout(null);

        add(JBurst.defaultCam);

        burstThread.start();
        setVisible(true);
    }

    private void reset()
    {
        try
        {
            _requestedState = _initialState.getConstructor().newInstance();
        }
        catch(NoSuchMethodException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            System.out.println("Error instantiating initial state.\n" + e.getMessage());
            System.exit(0);
        }
    }

    private void switchState()
    {
        // JBurst.cameras.reset(); // For when I get around to cameras

        if(_state != null) _state.destroy();

        _state = JBurst.state = _requestedState;
        _state.create();
    }

    private void update()
    {
        if(!_state.active || !_state.exists) return;

        if(_state != _requestedState) switchState();

        JBurst.elapsed = elapsed;

        _state.update(elapsed);

        JBurst.defaultCam.update();
    }

    private long getTotal()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }
}
