package burst;

import java.awt.Color;
import java.awt.Graphics;

import burst.animation.BurstAnimationController;
import burst.graphics.frames.BurstFramesCollection;
import burst.graphics.BurstGraphic;

public class BurstSprite extends BurstBasic {
    public float x = 0;
    public float y = 0;
    public float width;
    public float height;

    public BurstAnimationController animation;

    public BurstGraphic frame;
    public int numFrames;
    public BurstFramesCollection frames;

    public BurstGraphic graphic;

    public float alpha = 1.0f;

    public BurstSprite() {
        this(0, 0);
    }

    public BurstSprite(int x, int y) {
        super();

        this.x = x;
        this.y = y;
        setLocation(x, y);

        this.width = 0;
        this.height = 0;

        animation = new BurstAnimationController(this);
    }

    public BurstSprite loadGraphic(BurstGraphic graphic, boolean animated, int width, int height) {
        if(width == 0) {
            width = animated ? graphic.height : graphic.width;
			width = (width > graphic.width) ? graphic.width : width;
        }

        if (height == 0)
		{
			height = animated ? width : graphic.height;
			height = (height > graphic.height) ? graphic.height : height;
		}

        frame = graphic;
        setSize(frame.data.getWidth(), frame.data.getHeight());

        return this;
    }

    @Override
    public void update(float elapsed) {
        super.update(elapsed);

        

        animation.update(elapsed);
    }

    @Override 
    public void paintComponent(Graphics g) {
        if(!visible || alpha == 0)
            return;

        //for(BurstCamera camera : cameras) {
            //if(!camera.visible || !camera.exists)
                //continue;

                for (int i = 1; i < frame.data.getHeight(); i++) {
                    for (int j = 1; j < frame.data.getWidth(); j++) {
                        //Color color = new Color(frame.image.getRGB(j, i));
    
                        g.setColor(new Color(frame.data.getRGB(j, i), true));
                        g.drawLine(j, i, j, i);
                    }
                }
        //}
    }
}
