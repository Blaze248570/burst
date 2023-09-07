package burst;

import java.awt.Dimension;

/**
 * @author Joe Bray
 * <p> Modeled from <a href="https://api.haxeflixel.com/flixel/FlxG.html">FlxG</a>
 */
public class JBurst
{
    public static Dimension size;

    private static JBurstGame game;

    public static JBurstState state;

    /**
     * The default camera that objects are sent to.
     */
    public static JBurstCamera defaultCam;

    // public static JBurstKeyboard keys;

    /**
     * Measured time between update() calls in milliseconds
     */
    public static double elapsed;

    protected static void init(JBurstGame game, Dimension size)
    {
        JBurst.game = game;
        JBurst.size = size;

        defaultCam = new JBurstCamera();

        /*
            keys = new JBurstKeyboard();
            game.addKeyListener(keys);
            game.setFocusable(true);
        */
    }

    public static void switchState(JBurstState nextState)
    {
        state.startOutro(() -> {
            game._requestedState = nextState;
        });
    }
}
