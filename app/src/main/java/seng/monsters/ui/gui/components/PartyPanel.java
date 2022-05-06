//
//  PartyPanel.java
//  seng-monsters
//
//  Created by d-exclaimation on 15:24.
//  Copyright © 2022 d-exclaimation. All rights reserved.
//
package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;
import seng.monsters.model.Trainer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.IntStream;

/**
 * A Pokemon-like display for a party
 */
public final class PartyPanel {

    /**
     * The trainer to display
     */
    private final Trainer trainer;

    /**
     * The display panel
     */
    private JPanel panel;

    /**
     * The panel for all monster slots
     */
    private List<PartySlotPanel> slots;

    /**
     * The action to be performed if a slot is clicked
     */
    private BiConsumer<ActionEvent, Monster> action;

    /**
     * A Pokemon-like display for a party
     *
     * @param trainer The trainer to display
     */
    public PartyPanel(Trainer trainer) {
        this.trainer = trainer;
        this.action = (e, m) -> {
        };
        render();
    }

    /**
     * Set the boundary / location of the panel on the screen
     *
     * @param x The horizontal location
     * @param y The vertical location
     */
    public void setBounds(int x, int y) {
        panel.setBounds(x, y, WIDTH, HEIGHT);
        panel.setLayout(null);
    }

    /**
     * Add this panel to the window frame
     *
     * @param frame The JFrame to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToFrame(JFrame frame) {
        frame.getContentPane().add(panel);
    }

    /**
     * Set the action to be performed when slot is clicked
     *
     * @param action The action callback
     */
    public void onAction(BiConsumer<ActionEvent, Monster> action) {
        this.action = action;
    }

    /**
     * Initialize the UI element for this panel
     */
    private void render() {
        panel = new JPanel();
        panel.setOpaque(false);

        final var party = trainer.getParty();
        final var diffY = 20;

        slots = IntStream.range(0, party.size())
            .mapToObj(i -> {
                PartySlotPanel slot = new PartySlotPanel(party.get(i));
                slot.setBounds(0, i * (PartySlotPanel.HEIGHT + diffY));
                slot.applyToPanel(panel);
                slot.addActionListener(eachPanelAction(i));
                return slot;
            })
            .toList();
    }


    /**
     * Refresh the party display
     */
    public void refresh() {
        for (var i = 0; i < trainer.getParty().size(); i++) {
            final var newMon = trainer.getParty().get(i);
            slots.get(i).refresh(newMon);
            slots.get(i).setVisible(true);
        }
        for (var i = trainer.getParty().size(); i < slots.size(); i++) {
            slots.get(i).setVisible(false);
        }
    }

    /**
     * Action performed when a slot is pressed
     *
     * @param i The slot index
     * @return An action listener for the button in the slot
     */
    private ActionListener eachPanelAction(int i) {
        return e -> action.accept(e, trainer.getParty().get(i));
    }

    /**
     * The width of the panel
     */
    public static final int WIDTH = PartySlotPanel.WIDTH;

    /**
     * The height of the panel
     */
    public static final int HEIGHT = PartySlotPanel.HEIGHT * 4 + 3 * (20);
}