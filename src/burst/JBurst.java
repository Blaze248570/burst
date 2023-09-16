package burst;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JFrame;

/**
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst
{
    public static Dimension size;

    /**
     * The default camera that objects are sent to.
     */
    public static JBurstCamera defaultCam;

    public boolean active = true;

    private JFrame frame;

    public JBurstState state;

    private JBurstState _requestedState;

    private final Class<? extends JBurstState> _initialState;

    // public JBurstKeyboard keys;

    /**
     * Time in milliseconds since program began
     */
    private long _total;

    /**
     * Measured time between update() calls in milliseconds
     */
    public double elapsed;

    private final Instant _startTime = Instant.now();

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

    public JBurst(JFrame frame, Class<? extends JBurstState> initialState)
    {
        this.frame = frame;
        frame.addComponentListener(new FrameListener());
        JBurst.size = frame.getSize();

        this._initialState = initialState;

        defaultCam = new JBurstCamera();

        /*
            keys = new JBurstKeyboard();
            game.addKeyListener(keys);
            game.setFocusable(true);
        */

        _total = getTotal();

        reset();
        switchState();

        frame.add(defaultCam);

        burstThread.run();
    }

    private void update()
    {
        if(!active || !state.active || !state.exists) return;

        if(state != _requestedState) switchState();

        state.update(elapsed);

        defaultCam.update();
    }

    public void switchState(JBurstState nextState)
    {
        state.startOutro(() -> {
            _requestedState = nextState;
        });
    }

    private void switchState()
    {
        if(state != null) state.destroy();

        state = _requestedState;
        state.create();
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

    private long getTotal()
    {
        return Duration.between(_startTime, Instant.now()).toMillis();
    }

    private class FrameListener implements ComponentListener
    {

        @Override
        public void componentResized(ComponentEvent e) 
        {
            JBurst.size = frame.getSize();
        }

        @Override
        public void componentMoved(ComponentEvent e) { }

        @Override
        public void componentShown(ComponentEvent e) 
        {
            active = true;
        }

        @Override
        public void componentHidden(ComponentEvent e) 
        {
            active = false;
        }
        
    }
}
