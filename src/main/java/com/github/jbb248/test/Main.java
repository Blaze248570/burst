package com.github.jbb248.test;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.github.jbb248.jburst.JBurstSprite;

public class Main
{
    public static JFrame window;
    public static final Dimension SIZE = new Dimension(640, 720);

    public static void main(String[] args) 
    {
        window = new JFrame("JBurst Demo Application");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*
         * Center the window on the screen 
         */
        Dimension resolution = Toolkit.getDefaultToolkit().getScreenSize();
        int windowX = (int) (resolution.getWidth() / 2 - SIZE.width / 2);
        int windowY = (int) (resolution.getHeight() / 2 - SIZE.height / 2);
        window.setLocation(windowX, windowY);
        window.setSize(SIZE);
        window.setLayout(null);

        SwingUtilities.invokeLater(Main::create);
    }

    public static final int OBJ_WIDTH = 125, OBJ_HEIGHT = 30;
    public static JComboBox<String> dropDown;
    public static JCheckBox showBoundsBox, spinBox;

    public static PichuSprite pichu;
    public static RaichuSprite raichu;
    public static VaporeonSprite vaporeon;

    public static void create()
    {
        /*
         * This creates a red square with an "open window" that the pokemon will reside within.
         */
        JBurstSprite redBox = new JBurstSprite().makeGraphic(SIZE.width, SIZE.height, new Color(230, 86, 98));
        redBox.active = false; // Stops this sprite from going through its update process since it doesn't need to
        redBox.start();

        Rectangle whiteBox = new Rectangle((int)(redBox.getSpriteWidth() * 0.125), 50, (int)(redBox.getSpriteWidth() * 0.75), (int)(redBox.getSpriteWidth() * 0.75));
        Graphics2D sqPixels = redBox.getPixels(); // We can paint directly to the sprite's graphical data using getPixels()
        sqPixels.setColor(new Color(238, 238, 238));
        sqPixels.fillRoundRect(whiteBox.x, whiteBox.y, whiteBox.width, whiteBox.height, 25, 25);

        /*
         * This creates a dropdown selector that will allow the user to select which pokemon to display. 
         */
        dropDown = new JComboBox<>(new String[] {"Pichu", "Raichu", "Vaporeon"});
        dropDown.setLocation(whiteBox.x + 5, whiteBox.y + whiteBox.height + 15);
        dropDown.setSize(85, OBJ_HEIGHT);
        dropDown.addActionListener(new DropDownListener());

        JLabel dropDownLabel = new JLabel("  Pokemon");
        dropDownLabel.setLocation(dropDown.getX() + dropDown.getWidth(), dropDown.getY());
        dropDownLabel.setSize(70, OBJ_HEIGHT);
        dropDownLabel.setOpaque(true);
        dropDownLabel.setBackground(new Color(238, 238, 238));

        /*
         * This creates a checkbox that we'll use to toggle whether the sprite boundaries should
         * be displayed or not. 
         */
        showBoundsBox = new JCheckBox("Toggle Bounds");
        showBoundsBox.setLocation(dropDown.getX(), dropDown.getY() + OBJ_HEIGHT + 10);
        showBoundsBox.setSize(OBJ_WIDTH, OBJ_HEIGHT);
        showBoundsBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                pichu.debugMode = !pichu.debugMode;            
                raichu.debugMode = !raichu.debugMode;
                vaporeon.debugMode = !vaporeon.debugMode;
            } 
        });

        /*
         * This creates a checkbox that we'll use to toggle whether the sprite should spin clockwise
         */
        spinBox = new JCheckBox("Spin Sprite");
        spinBox.setLocation(showBoundsBox.getX(), showBoundsBox.getY() + OBJ_HEIGHT + 10);
        spinBox.setSize(OBJ_WIDTH, OBJ_HEIGHT);
        spinBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                pichu.spin = !pichu.spin;            
                raichu.spin = !raichu.spin;
                vaporeon.spin = !vaporeon.spin;
            }
        });

        /*************************************************************/

        pichu = new PichuSprite();
        int pichuX = redBox.getSpriteX() + (redBox.getSpriteWidth() / 2) - (pichu.getSpriteWidth() / 2);
        int pichuY = (SIZE.height / 2) - (pichu.getSpriteHeight() / 2);
        pichu.setSpriteLocation(pichuX, pichuY); // Calculations to center the Pichu within the white box
        pichu.start();

        raichu = new RaichuSprite();
        raichu.setScale(0.75);
        int raichuX = redBox.getSpriteX() + (redBox.getSpriteWidth() / 2) - (raichu.getSpriteWidth() / 2);
        int raichuY = (SIZE.height / 2) - (raichu.getSpriteHeight() / 2) - 80;
        raichu.setSpriteLocation(raichuX, raichuY); // Calculations to center the Raichu within the white box

        vaporeon = new VaporeonSprite();
        vaporeon.setScale(0.75);
        int vaporeonX = redBox.getSpriteX() + (redBox.getSpriteWidth() / 2) - (vaporeon.getSpriteWidth() / 2);
        int vaporeonY = (SIZE.height / 2) - (vaporeon.getSpriteHeight() / 2) - 80;
        vaporeon.setSpriteLocation(vaporeonX, vaporeonY); // Calculations to center the Vaporeon within the white box

        /*************************************************************/

        Container pane = window.getContentPane();

        /*
         * Object heirarchy in Java Swing goes by descending order.
         * In other words, the first object added will always be on top.
         */
        pane.add(showBoundsBox);
        pane.add(dropDown);
        pane.add(dropDownLabel);
        pane.add(spinBox);

        pane.add(pichu);
        pane.add(raichu);
        pane.add(vaporeon);
        pane.add(redBox);

        /*
         * Any time a new component is added, the window must be revalidated
         * in order for it to be acknowledged.
         * It's not necessary here as setVisible() will revalidate the window,
         * but it's good practice to include at the end of functions that 
         * add or remove components.
         */
        window.revalidate();
        window.setVisible(true);
    }

    /*
     * Listeners are common classes used to manage JComponents 
     * and give them instructions when they're interected with.
     */
    static class DropDownListener implements ActionListener
    {
        @Override
        @SuppressWarnings("rawtypes")
        public void actionPerformed(ActionEvent e)
        {
            JComboBox source = (JComboBox) e.getSource();
            String selected = (String) source.getSelectedItem();

            switch(selected)
            {
                case "Raichu":
                    pichu.stop();
                    raichu.start();
                    vaporeon.stop();
                break;
                case "Vaporeon":
                    pichu.stop();
                    raichu.stop();
                    vaporeon.start();
                break;
                default:
                    pichu.start();
                    raichu.stop();
                    vaporeon.stop();
            }
        }
    }
}