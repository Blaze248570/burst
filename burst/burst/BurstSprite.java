package burst;

import java.awt.Color;
import java.awt.Graphics;

import burst.animation.BurstAnimationController;
import burst.graphics.BurstGraphic;

public class BurstSprite extends BurstBasic {
    public BurstAnimationController animation;
    public BurstGraphic frame;

    public int x = 0;
    public int y = 0;
    public int width = 500;
    public int height = 500;

    public float alpha = 1;

    public BurstSprite() {
        this(0, 0);
    }

    public BurstSprite(int x, int y) {
        super();
        this.x = x;
        this.y = y;
        setLocation(x, y);
        animation = new BurstAnimationController(this);
    }

    public void loadGraphic(BurstGraphic graphic) {
        frame = graphic;
    }

    @Override 
    public void paintComponent(Graphics g) {
        if(!visible || alpha == 0)
            return;

        //for(BurstCamera camera : cameras) {
            //if(!camera.visible || !camera.exists)
                //continue;

                for (int i = 1; i < frame.image.getHeight(); i++) {
                    for (int j = 1; j < frame.image.getWidth(); j++) {
                        //Color color = new Color(frame.image.getRGB(j, i));
    
                        g.setColor(new Color(frame.image.getRGB(j, i), true));
                        g.drawLine(j, i, j, i);
                    }
                }
        //}
    }
}
