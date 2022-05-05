//
//  PopUp.java
//  seng-monsters
//
//  Created by d-exclaimation on 19:22.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.components;

import seng.monsters.ui.gui.Screen;

import javax.swing.*;
import java.awt.*;

/**
 * Pop up abstract class to set up a frame for a functional pop-up
 */
public abstract class PopUp {

    /**
     * Window frame
     */
    protected final JFrame frame;

    public PopUp() {
        // Set the frame and its boundary
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(255, 255, 204));
        frame.getContentPane().setForeground(new Color(0, 0, 0));
        frame.getContentPane().setLayout(null);
        frame.setBounds(100, 100, WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
    }

    /**
     * The width of this pop-up
     */
    public static int WIDTH = Screen.WIDTH;

    /**
     * The height of this pop-up
     */
    public static int HEIGHT = Screen.HEIGHT;
}
