package seng.monsters.ui.gui.components;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.ui.gui.state.LabelComboboxModel;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private BiConsumer<ActionEvent, Monster> onChosen = (ignoredEvent, m) -> {
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

        partyComboBox.addActionListener(
            comboBoxSelectionAction(partyComboBox)
        );

        submitButton.addActionListener(
            submitAction(partyComboBox)
        );

        frame.setVisible(true);
    }

    /**
     * Set the callback to be run when the user has chosen a monster from their party
     *
     * @param function The callback that takes the action event and the selected monster
     */
    public void onChosen(BiConsumer<ActionEvent, Monster> function) {
        onChosen = function;
    }

    /**
     * The action performed when a selection is made in the combox
     *
     * @param comboBox The combo box to get the selection
     * @return An action listener for the combo box
     */
    private ActionListener comboBoxSelectionAction(JComboBox<String> comboBox) {
        return ignoredEvent -> {
            final var index = comboBox.getSelectedIndex();
            if (index < 0)
                return;
            chosenMonster.set(party.get(index));
        };
    }

    /**
     * The action performed when the selection is submitted
     *
     * @param comboBox The combo box to disable changes
     * @return An action listener for the submit button
     */
    private ActionListener submitAction(JComboBox<String> comboBox) {
        return ignoredEvent -> {
            final var monster = chosenMonster.get();
            comboBox.setEnabled(false);

            onChosen.accept(ignoredEvent, monster);
            frame.dispose();
        };
    }
}
