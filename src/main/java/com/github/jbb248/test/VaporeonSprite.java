package com.github.jbb248.test;

import com.github.jbb248.jburst.JBurstSprite;
import com.github.jbb248.jburst.graphics.frames.JBurstAtlasFrames;

/**
 * An extended class of JBurstSprite used to draw a Vaporeon.
 * <p>
 * This class employs the {@code fromSparrow()} method to parse its spritesheet.
 */
public class VaporeonSprite extends JBurstSprite
{
    public final String file_location = "assets/Vaporeon";

    public boolean spin = false;

    public VaporeonSprite()
    {
        super();

        // Load frames from sparrow texture pack
        setFrames(JBurstAtlasFrames.fromSparrow(file_location + ".png", file_location + ".xml"));

        animation.addByPrefix("dance", "Vaporeon Idle Dance", 24);
        animation.play("dance");
        kill();
    }

    @Override
    public void update(double elapsed)
    {
        super.update(elapsed);

        if(spin)
            setAngle(getAngle() + Math.toRadians(elapsed * 150));
        else
            setAngle(0.0);
    }
}
