package burst.graphics.frames;

import java.awt.Point;
import java.awt.Rectangle;

import burst.graphics.BurstGraphic;

public class BurstFrame {
    public String name;

    public Rectangle frame;

    public BurstGraphic parent;
    public FlxFrameType type;

    public Point sourceSize;

    /**
     * Frame offset from top left corner of original image
     */
    public Point offset;

    public BurstFrame(BurstGraphic parent) {
        this.parent = parent;

        type = FlxFrameType.REGULAR;

        sourceSize = new Point();
        offset = new Point();
    }

    public enum FlxFrameType {
        REGULAR;
    }
}
