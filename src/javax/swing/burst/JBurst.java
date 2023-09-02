package javax.swing.burst;

import java.awt.Dimension;

public class JBurst
{
    public static Dimension size;

    public static JBurstGame game;

    public static JBurstState state;

    /**
     * The default camera that objects are sent to.
     */
    public static JBurstCamera defaultCam;

    /**
     * Measured time between update() calls in milliseconds
     */
    public static double elapsed;

    protected static void init(JBurstGame game, Dimension size)
    {
        JBurst.game = game;
        JBurst.size = size;

        defaultCam = new JBurstCamera();
    }

    public static void switchState(JBurstState nextState)
    {
        state.startOutro(() -> {
            game._requestedState = nextState;
        });
    }
}
