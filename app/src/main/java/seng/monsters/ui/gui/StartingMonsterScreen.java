package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.ui.gui.components.DetailedMonsterPanel;
import seng.monsters.ui.gui.components.JoiningPopUp;
import seng.monsters.ui.gui.state.LabelComboboxModel;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

/**
 * A screen as part of the setup process to prompt the user with a selection of monster to start with
 */
public class StartingMonsterScreen extends Screen {

    /**
     * The options of monsters available
     */
    private final List<Monster> startingMonsters;

    /**
     * The selected monster state
     */
    private final State<Monster> selectedMonster;

    /**
     * Create an active GUI screen for selecting the starting monster
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public StartingMonsterScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        startingMonsters = Monster.all(5 - gameManager.getDifficulty()).subList(0, gameManager.getDifficulty() + 2);
        selectedMonster = State.of(startingMonsters.get(0));
    }

    @Override
    public void render() {
        // Combobox containing the starting monster choices
        JComboBox<String> startingMonsterComboBox = new JComboBox<>();
        startingMonsterComboBox.setModel(
            new LabelComboboxModel<>(startingMonsters, Monster::monsterType)
        );
        startingMonsterComboBox.setSelectedIndex(0);
        startingMonsterComboBox.setBounds(66, 140, 238, 27);
        frame.getContentPane().add(startingMonsterComboBox);

        JLabel startingMonsterPrompt = new JLabel("Choose your starting monster:");
        startingMonsterPrompt.setBounds(66, 112, 238, 16);
        frame.getContentPane().add(startingMonsterPrompt);

        // Panel displaying monster and its stats
        DetailedMonsterPanel panel = new DetailedMonsterPanel(selectedMonster.get(), false);
        panel.setBounds(439, 90);
        panel.applyToFrame(frame);

        // Button to submit monster selection
        JButton submitButton = new JButton("Next");
        submitButton.setBounds(350, 374, 117, 29);
        frame.getContentPane().add(submitButton);

        // Setting the on change callback for the selected monster
        selectedMonster.onChange(panel::refresh);

        startingMonsterComboBox.addActionListener(
            comboBoxAction(startingMonsterComboBox)
        );

        submitButton.addActionListener(
            submitAction(startingMonsterComboBox)
        );

        frame.setVisible(true);
    }

    /**
     * The action performed when the combo box selection changes
     *
     * @param comboBox The combo box to get the monster selection
     * @return An action listener for the combobox
     */
    private ActionListener comboBoxAction(JComboBox<String> comboBox) {
        return ignoredEvent -> {
            final int index = comboBox.getSelectedIndex();
            if (index < 0)
                return;
            selectedMonster.set(startingMonsters.get(index));
        };
    }

    /**
     * The action performed when the user has chosen their starting monster.
     *
     * @param comboBox The combo box for the starting monster
     * @return The action listener for the submit button
     */
    private ActionListener submitAction(JComboBox<String> comboBox) {
        return ignoredEvent -> {
            final Monster monster = selectedMonster.get();
            comboBox.setEnabled(false);

            // Displays popup to name monster
            final JoiningPopUp popUp = new JoiningPopUp(monster);
            popUp.onEnd(
                popUpRenameAction(monster)
            );
        };
    }

    /**
     * The action after the user chosen a new name for the monster in the pop-up.
     *
     * @param monster The joining monster to be renamed
     * @return The action for the pop-up after renaming
     */
    private Consumer<ActionEvent> popUpRenameAction(Monster monster) {
        return ignoredEvent -> {
            gameManager.getPlayer().add(monster);

            gui.navigateTo(new MainMenuScreen(gui, gameManager));
        };
    }
}
