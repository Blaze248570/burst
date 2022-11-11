package burst;

import burst.graphics.BurstGraphic;

public class Main {
    public static void main(String[] args) {
        Burst app = new Burst();

        BurstSprite busty = new BurstSprite(40, 40);
        busty.loadGraphic(BurstGraphic.fromBuffImage("assets/images/chester/frame0000.png", "chester"));
        busty.setBounds(40, 40, 600, 600);
        app.add(busty);

        app.setVisible(true);
    }
}

//BurstSprite picu = new BurstSprite("assets/images/chester/", 0, 0);
        //picu.animation.addEnMasse("Idle", "frame", new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43}, 100, true);
        //frame.add(picu);
        //picu.animation.play("Idle");

        /*BurstSprite busty = new BurstSprite("assets/images/Busty", 50, 50);
        busty.animation.addViaSparrow("Idle", "Buster Idle Dance", 24, true);
        busty.animation.addViaSparrow("noteLeft", "Buster LEFT NOTE", 24, true);
        busty.animation.addViaSparrow("missLeft", "Buster LEFT MISS", 24, true);
        busty.animation.addViaSparrow("downNote", "Buster DOWN NOTE", 24, true);
        busty.animation.addViaSparrow("downMiss", "Buster DOWN MISS", 24, true);
        busty.animation.addViaSparrow("upNote", "Buster UP NOTE", 24, true);
        busty.animation.addViaSparrow("upMiss", "Buster UP MISS", 24, true);
        busty.animation.addViaSparrow("rightNote", "Buster RIGHT NOTE", 24, true);
        busty.animation.addViaSparrow("rightMiss", "Buster RIGHT MISS", 24, true);*/
