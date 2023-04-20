package burst;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class BurstCamera extends java.awt.Container
{
    public Burst parent;
    public BufferedImage data;

    public float alpha = 0.0f;

    public BurstCamera(Burst parent)
    {
        this.parent = parent;

        setBounds(0, 0, parent.getWidth(), parent.getHeight());
    }
    
    @Override 
    public void paint(Graphics graphics)
    {
        // Paint me
        // graphics.drawImage(data, 0, 0, );

        // Then paint my children
        System.out.println("Painting my children: \n" + java.util.Arrays.toString(getComponents()));
        System.out.println(this.getComponent(0).getBounds());
        super.paint(graphics);
    }
}
