package seng.monsters.ui.gui.components;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.ui.gui.state.LabelComboboxModel;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Party Monster Selection Pop Up to prompt user to choose a monster from their party
 */
public final class SelectPartyPopUp extends PopUp {
    /**
     * The party of the user
     */
    private final List<Monster> party;

    /**
     * The callback when the user has chose a monster
     */
    private BiConsumer<ActionEvent, Monster> onChosen = (e, m) -> {
    };

    /**
     * The chosen monster state
     */
    private final State<Monster> chosenMonster;

    public SelectPartyPopUp(GameManager gameManager) {
        this.party = gameManager.getTrainer().getParty();
        this.chosenMonster = State.of(party.get(0));
        render();
    }

    private void render() {
        JComboBox<String> partyComboBox = new JComboBox<>();
        partyComboBox.setModel(
            new LabelComboboxModel<>(party, Monster::getName)
        );
        partyComboBox.setSelectedIndex(0);
        partyComboBox.setBounds(66, 140, 238, 27);
        frame.getContentPane().add(partyComboBox);

        JLabel promptLabel = new JLabel("Choose your monster from the party:");
		promptLabel.setBounds(66, 112, 238, 16);
        frame.getContentPane().add(promptLabel);

        DetailedMonsterPanel panel = new DetailedMonsterPanel(chosenMonster.get(), false);
        panel.setBounds(439, 90);
        panel.applyToFrame(frame);

        JButton submitButton = new JButton("Next");
        submitButton.setBounds(350, 374, 117, 29);
        frame.getContentPane().add(submitButton);

        // Setting the on change callback for the selected monster
        chosenMonster.onChange(panel::refresh);

        partyComboBox.addActionListener(e -> {
            if (partyComboBox.getSelectedIndex() < 0)
                return;
            chosenMonster.set(party.get(partyComboBox.getSelectedIndex()));
        });

        submitButton.addActionListener(e -> {
            final var monster = chosenMonster.get();
            partyComboBox.setEnabled(false);

            onChosen.accept(e, monster);
            frame.dispose();
        });

        frame.setVisible(true);
    }

    /**
     * Set the callback to be run when the user has chosen a monster from their party
     * @param function The callback that takes the action event and the selected monster
     */
    public void onChosen(BiConsumer<ActionEvent, Monster> function) {
        onChosen = function;
    }
}
