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
     * The callback when the user has chosen a monster
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
        this.trainer = gameManager.getPlayer();
        this.chosenMonster = State.of(trainer.getParty().get(0));
        render();
    }

    private void render() {
        // Panel of the player's party
        PartyPanel partyPanel = new PartyPanel(trainer);
        partyPanel.setBounds(66, 70);
        partyPanel.applyToFrame(frame);

        JLabel promptLabel = new JLabel("Choose a monster from your party:");
        promptLabel.setBounds(66, 50, 238, 16);
        frame.getContentPane().add(promptLabel);

        // Detailed panel displaying the monster and its stats
        DetailedMonsterPanel panel = new DetailedMonsterPanel(chosenMonster.get(), false);
        panel.setBounds(439, 90);
        panel.applyToFrame(frame);

        // Button to confirm selection
        JButton submitButton = new JButton("Select");
        submitButton.setBounds(456, 100 + PartyPanel.HEIGHT, 117, 29);
        frame.getContentPane().add(submitButton);

        // Button to cancel the action
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setBounds(206, 100 + PartyPanel.HEIGHT, 117, 29);
        frame.getContentPane().add(cancelButton);

        // Setting the on change callback for the selected monster
        chosenMonster.onChange(panel::refresh);

        partyPanel.onAction(partySelectionAction());
        submitButton.addActionListener(submitAction());
        cancelButton.addActionListener(cancelAction());

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
        return (ignoredEvent, monster) -> chosenMonster.set(monster);
    }

    /**
     * The action performed when the select button is used (confirms monster selection and performs the action)
     *
     * @return An action listener for the submit button
     */
    private ActionListener submitAction() {
        return ignoredEvent -> {
            final Monster monster = chosenMonster.get();

            onChosen.accept(ignoredEvent, monster);
            frame.dispose();
        };
    }

    /**
     * The action performed when the cancel button is used (cancels the attempted action)
     *
     * @return An action listener for the cancel button.
     */
    private ActionListener cancelAction() {
        return ignoredEvent -> frame.dispose();
    }
}
