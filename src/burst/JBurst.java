package burst;

import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JFrame;

import burst.group.JBurstGroup;

/**
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst extends JBurstGroup<JBurstBasic>
{
    public static Dimension size;

    public boolean active = true;

    private JFrame frame;

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

    public JBurst(JFrame frame)
    {
        super();

        this.frame = frame;
        frame.addComponentListener(new FrameListener());
        JBurst.size = frame.getSize();

        /*
            keys = new JBurstKeyboard();
            game.addKeyListener(keys);
            game.setFocusable(true);
        */

        _total = getTotal();

        burstThread.start();
    }

    private void update()
    {
        if(!active || members.size() == 0) return;

        update(elapsed);

        for(int i = 0; i < members.size(); i++)
        {
            JBurstBasic basic = members.get(i);
            if(basic != null && basic.exists && basic.active)
            {
                basic.repaint();
            }
        }
    }

    @Override
    public JBurstBasic add(JBurstBasic basic)
    {
        frame.add(basic);

        return super.add(basic);
    }

    @Override
    public JBurstBasic add(int index, JBurstBasic basic)
    {
        frame.add(basic, index);

        return super.add(index, basic);
    }

    @Override
    public JBurstBasic remove(JBurstBasic basic)
    {
        frame.remove(basic);

        return super.remove(basic, true);
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
