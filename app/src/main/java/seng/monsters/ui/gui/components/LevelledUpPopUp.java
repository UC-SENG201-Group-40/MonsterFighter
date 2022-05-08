//
//  LevelledUpPopUp.java
//  seng-monsters
//
//  Created by d-exclaimation on 11:44.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LevelledUpPopUp extends PopUp {
    /**
     * Monster that has joined
     */
    private final List<Monster> monsters;

    /**
     * Create the application.
     */
    public LevelledUpPopUp(List<Monster> monsters) {
        this.monsters = monsters;
        render();
    }

    private void render() {
        // Title label using the name of the monster
        JLabel titleLabel = new JLabel(
            String.format("%d of your monster(s) has levelled up", monsters.size())
        );
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
        titleLabel.setBounds((PopUp.WIDTH - 700) / 2, 32, 700, 39);
        frame.getContentPane().add(titleLabel);

        // The icon for the monster joining
        final var diffY = 20;

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(null);
        panel.setBounds((PopUp.WIDTH - PartySlotPanel.WIDTH) / 2, 71, PartySlotPanel.WIDTH, PartySlotPanel.WIDTH * 4 + 3 * diffY);
        for (var i = 0; i < monsters.size(); i++) {
            PartySlotPanel slot = new PartySlotPanel(monsters.get(i));
            slot.setBounds(0, i * (PartySlotPanel.HEIGHT + diffY));
            slot.applyToPanel(panel);
        }
        frame.getContentPane().add(panel);

        // The button to apply the name change and trigger the onEnd callback
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(351, 400, 117, 29);
        frame.getContentPane().add(continueButton);

        continueButton.addActionListener(e -> frame.dispose());

        frame.setVisible(true);
    }
}
