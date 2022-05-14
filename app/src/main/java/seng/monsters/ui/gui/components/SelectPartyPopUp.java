package seng.monsters.ui.gui.components;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.BiConsumer;

/**
 * Party Monster Selection Pop Up to prompt user to choose a monster from their party
 */
public final class SelectPartyPopUp extends PopUp {
    /**
     * The party of the user
     */
    private final Trainer trainer;

    /**
     * The callback when the user has chose a monster
     */
    private BiConsumer<ActionEvent, Monster> onChosen = (ignoredEvent, ignoredMonster) -> {
    };

    /**
     * The chosen monster state
     */
    private final State<Monster> chosenMonster;

    /**
     * Creates a pop-up screen to allow user to select a monster from their party
     * @param gameManager The game manager / controller
     */
    public SelectPartyPopUp(GameManager gameManager) {
        this.trainer = gameManager.getTrainer();
        this.chosenMonster = State.of(trainer.getParty().get(0));
        render();
    }

    private void render() {
        PartyPanel partyPanel = new PartyPanel(trainer);
        partyPanel.setBounds(66, 110);
        partyPanel.applyToFrame(frame);

        JLabel promptLabel = new JLabel("Choose your monster from the party:");
        promptLabel.setBounds(66, 90, 238, 16);
        frame.getContentPane().add(promptLabel);

        DetailedMonsterPanel panel = new DetailedMonsterPanel(chosenMonster.get(), false);
        panel.setBounds(439, 90);
        panel.applyToFrame(frame);

        JButton submitButton = new JButton("Next");
        submitButton.setBounds(350, 120 + PartyPanel.HEIGHT, 117, 29);
        frame.getContentPane().add(submitButton);

        // Setting the on change callback for the selected monster
        chosenMonster.onChange(panel::refresh);

        partyPanel.onAction(partySelectionAction());
        submitButton.addActionListener(submitAction());

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
     * The action performed when a selection is made in the party panel
     *
     * @return An action listener for the party panel
     */
    private BiConsumer<ActionEvent, Monster> partySelectionAction() {
        return (ignoredEvent, monster) -> {
            chosenMonster.set(monster);
        };
    }

    /**
     * The action performed when the selection is submitted
     *
     * @return An action listener for the submit button
     */
    private ActionListener submitAction() {
        return ignoredEvent -> {
            final var monster = chosenMonster.get();

            onChosen.accept(ignoredEvent, monster);
            frame.dispose();
        };
    }
}
