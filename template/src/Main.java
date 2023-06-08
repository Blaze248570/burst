import burst.JBurst;
import burst.JBurstSprite;
import burst.graphics.JBurstGraphic;

public class Main 
{
    public static void main(String[] args) 
    {
        JBurst app = new JBurst(800, 700);

        JBurstSprite pichu = new JBurstSprite(10, 230);
        pichu.loadAnimatedGraphic(JBurstGraphic.fromFile("assets/images/pichu_sheet.png", "pichu"), 175, 175);
        int[] frames = new int[44];
        for(int i = 0; i < 44; i++) frames[i] = i;
        pichu.animation.add("Dance", frames, 30, true);
        pichu.animation.play("Dance");


        JBurstSprite alolan_raichu = new JBurstSprite(0, 0);
        alolan_raichu.loadFrames("assets/images/Alolan_Raichu.png", "assets/images/Alolan_Raichu.xml");
        alolan_raichu.animation.addByPrefix("dance", "Raichu Idle Dance", 24, true);
        alolan_raichu.animation.play("dance", true, false, 0);
        

        JBurstSprite vaporeon = new JBurstSprite(120, 120);
        vaporeon.loadFrames("assets/images/Vaporeon.png", "assets/images/Vaporeon.xml");
        vaporeon.animation.addByPrefix("dance", "Vaporeon Idle Dance", 24, true);
        vaporeon.animation.play("dance");

        app.add(pichu);
        app.add(alolan_raichu);
        app.add(vaporeon);

        app.setVisible(true);

        while(true) 
        {
            app.update();
        }
    }
}