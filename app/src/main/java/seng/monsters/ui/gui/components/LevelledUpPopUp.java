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

/**
 * A pop-up screen to show all monsters in the party that have levelled up
 */
public class LevelledUpPopUp extends PopUp {
    /**
     * Monster that has joined
     */
    private final List<Monster> monsters;

    /**
     * Create a pop-up screen to allow the player to see monsters in the party who levelled up
     *
     * @param monsters The list of monsters that have just levelled up
     */
    public LevelledUpPopUp(List<Monster> monsters) {
        this.monsters = monsters;
        render();
    }

    private void render() {
        // Title label showing how many monsters have levelled up
        JLabel titleLabel = new JLabel(
            String.format("%d of your monster(s) has levelled up", monsters.size())
        );
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
        titleLabel.setBounds((PopUp.WIDTH - 700) / 2, 32, 700, 39);
        frame.getContentPane().add(titleLabel);


        final int diffY = 20;

        // Panel containing the party slots of the monsters that levelled up
        JPanel levelledUpMonstersPanel = new JPanel();
        levelledUpMonstersPanel.setOpaque(false);
        levelledUpMonstersPanel.setLayout(null);
        levelledUpMonstersPanel.setBounds((PopUp.WIDTH - PartySlotPanel.WIDTH) / 2, 71, PartySlotPanel.WIDTH, PartySlotPanel.WIDTH * 4 + 3 * diffY);
        for (int i = 0; i < monsters.size(); i++) {
            PartySlotPanel slot = new PartySlotPanel(monsters.get(i));
            slot.setBounds(0, i * (PartySlotPanel.HEIGHT + diffY));
            slot.applyToPanel(levelledUpMonstersPanel);
        }
        frame.getContentPane().add(levelledUpMonstersPanel);

        // The button to close the popup and continue the game
        JButton continueButton = new JButton("Continue");
        continueButton.setBounds(351, 400, 117, 29);
        frame.getContentPane().add(continueButton);

        continueButton.addActionListener(ignoredEvent -> frame.dispose());

        frame.setVisible(true);
    }
}
