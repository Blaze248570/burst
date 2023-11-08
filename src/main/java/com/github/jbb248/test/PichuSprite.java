package com.github.jbb248.test;

import java.util.stream.IntStream;

import com.github.jbb248.jburst.JBurstSprite;

/**
 * An extended class of JBurstSprite used to draw a Pichu.
 * <p>
 * This class employs basic parsing methods to interpret its spritesheet.
 */
public class PichuSprite extends JBurstSprite
{
    public final String file_location = "assets/pichu_sheet.png";

    /**
     * Used to determine which frames from the pichu spritesheet should make up the animation.
     */
    public final int[] indices = IntStream.range(0, 44).toArray();

    public boolean spin = false;

    public PichuSprite()
    {
        super();

        // Load frames from basic tilesheet
        loadAnimatedGraphic(file_location, 175, 175);
        
        animation.add("Dance", indices);
        animation.play("Dance");
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
