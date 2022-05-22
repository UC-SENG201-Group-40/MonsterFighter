//
//  MonsterLeavingPopUp.java
//  seng-monsters
//
//  Created by d-exclaimation on 11:29.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;
import seng.monsters.ui.gui.Screen;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * A pop up to show if a monster is leaving
 */
public class LeavingPopUp extends PopUp {
    /**
     * Monster that has joined
     */
    private final Monster monster;

    /**
     * Create a pop-up screen to allow the player to see their leaving monster
     *
     * @param monster The monster that have just left
     */
    public LeavingPopUp(Monster monster) {
        this.monster = monster;
        render();
    }

    private void render() {
        // Title label using the name of the monster
        JLabel titleLabel = new JLabel(
            String.format("%s has left your party! Goodbye!", monster.getName())
        );
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
        titleLabel.setBounds((PopUp.WIDTH - 600) / 2, 52, 600, 39);
        frame.getContentPane().add(titleLabel);

        // The icon for the monster leaving
        JLabel iconLabel = new JLabel();
        Screen.imageIconFromResource(String.format("/images/%s.gif", monster.monsterType().toLowerCase()))
            .ifPresent(iconLabel::setIcon);
        iconLabel.setBounds((PopUp.WIDTH - 146) / 2, 160, 146, 171);
        frame.getContentPane().add(iconLabel);

        // The button to close the popup and continue the game
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(351, 400, 117, 29);
        frame.getContentPane().add(continueButton);

        continueButton.addActionListener(ignoredEvent -> frame.dispose());

        frame.setVisible(true);
    }
}
