//
//  MonsterLeavingPopUp.java
//  seng-monsters
//
//  Created by d-exclaimation on 11:29.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;

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
     * Create the application.
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

        // The icon for the monster joining
        JLabel iconLabel = new JLabel();
        iconLabel.setIcon(new ImageIcon(
            Objects.requireNonNull(LeavingPopUp.class.getResource(
                String.format("/images/%s.gif", monster.monsterType().toLowerCase())
            ))
        ));
        iconLabel.setBounds((PopUp.WIDTH - 146) / 2, 160, 146, 171);
        frame.getContentPane().add(iconLabel);

        // The button to apply the name change and trigger the onEnd callback
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(351, 400, 117, 29);
        frame.getContentPane().add(continueButton);

        continueButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}
