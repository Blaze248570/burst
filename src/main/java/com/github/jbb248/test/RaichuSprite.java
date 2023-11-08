package com.github.jbb248.test;

import com.github.jbb248.jburst.JBurstSprite;
import com.github.jbb248.jburst.graphics.frames.JBurstAtlasFrames;

/**
 * An extended class of JBurstSprite used to draw a Raichu.
 * <p>
 * This class employs the {@code fromJsonPacker()} method to parse its spritesheet.
 */
public class RaichuSprite extends JBurstSprite
{
    public final String file_location = "assets/Alolan_Raichu";

    public boolean spin = false;

    public RaichuSprite()
    {
        super();

        // Load frames from json texture pack
        setFrames(JBurstAtlasFrames.fromJsonPacker(file_location + ".png", file_location + ".json"));

        animation.addByPrefix("dance", "Raichu Idle Dance", 24);
        animation.play("dance", true, false, 0);
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
