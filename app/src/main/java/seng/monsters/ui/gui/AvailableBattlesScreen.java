package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.components.PartyPanel;
import seng.monsters.ui.gui.state.LabelComboboxModel;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * A screen to display the selection of enemy trainer to battle
 */
public class AvailableBattlesScreen extends Screen {

    /**
     * The selected enemy trainer state
     */
    private final State<Trainer> selectedTrainer;

    /**
     * Create an active GUI screen for showing all available battles
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public AvailableBattlesScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        selectedTrainer = State.of(gameManager.getAvailableBattles().get(0));
    }

    @Override
    public void render() {
        // Changes background to the current environment
        Screen
            .imageIconFromResource(
                String.format("/images/%s.jpeg", gameManager.getEnvironment().toString().toLowerCase())
            )
            .ifPresent(icon -> frame.setContentPane(new JLabel(icon)));

        // Combobox containing enemy choices
        JComboBox<String> enemiesComboBox = new JComboBox<>();
        enemiesComboBox.setModel(
            new LabelComboboxModel<>(gameManager.getAvailableBattles(), Trainer::getName)
        );
        enemiesComboBox.setSelectedIndex(0);
        enemiesComboBox.setBounds(66, 140, 238, 27);
        frame.getContentPane().add(enemiesComboBox);

        JLabel battlesPrompt = new JLabel("Select a trainer to fight:");
        battlesPrompt.setHorizontalAlignment(SwingConstants.CENTER);
        battlesPrompt.setBounds(66, 112, 238, 16);
        frame.getContentPane().add(battlesPrompt);

        // Panel displaying enemy's party
        PartyPanel enemyPanel = new PartyPanel(selectedTrainer.get());
        enemyPanel.setBounds(419, 80);
        enemyPanel.applyToFrame(frame);

        // Button to start the battle
        JButton battleButton = new JButton("Fight");
        battleButton.setBounds(475, 390, 117, 29);
        frame.getContentPane().add(battleButton);

        // Button to return to the main menu
        JButton cancelButton = new JButton("Return");
        cancelButton.setBounds(187, 390, 117, 29);
        frame.getContentPane().add(cancelButton);

        // Refresh the enemy party panel
        selectedTrainer.onChange(enemyPanel::refresh);

        enemiesComboBox.addActionListener(selectEnemyAction(enemiesComboBox));
        battleButton.addActionListener(battleAction(enemiesComboBox));
        cancelButton.addActionListener(backToMainMenuAction());

        frame.setVisible(true);
    }

    /**
     * The action performed when a new trainer is selected (sets the selectedTrainer)
     *
     * @param enemiesComboBox The enemy combobox to get the new selection
     * @return An action listener for the combo-box
     */
    private ActionListener selectEnemyAction(JComboBox<String> enemiesComboBox) {
        return ignoredEvent -> {
            final int index = enemiesComboBox.getSelectedIndex();
            if (index < 0 || index >= gameManager.getAvailableBattles().size())
                return;
            final Trainer newEnemy = gameManager.getAvailableBattles().get(index);
            selectedTrainer.set(newEnemy);
        };
    }

    /**
     * The action performed when using the fight button (Initiates a BattleScreen)
     *
     * @param enemiesComboBox The enemy combobox to get the new selection
     * @return An action listener for the battle button
     */
    private ActionListener battleAction(JComboBox<String> enemiesComboBox) {
        return ignoredEvent -> {
            final int index = enemiesComboBox.getSelectedIndex();
            gui.navigateTo(new BattleScreen(gui, gameManager, index));
        };
    }

    /**
     * The action performed when using the return (Returns to the main menu)
     *
     * @return An action listener for the return button
     */
    private ActionListener backToMainMenuAction() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }
}
