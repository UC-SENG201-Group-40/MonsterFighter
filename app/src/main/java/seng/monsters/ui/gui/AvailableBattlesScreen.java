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

    public void render() {
        frame.setContentPane(new JLabel(
            new ImageIcon(
                Objects.requireNonNull(BattleScreen.class.getResource(
                    String.format("/images/%s.jpeg", gameManager.getEnvironment().toString())
                )))
        ));

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

        PartyPanel panel = new PartyPanel(selectedTrainer.get());
        panel.setBounds(439, 80);
        panel.applyToFrame(frame);

        JButton battleButton = new JButton("Fight");
        battleButton.setBounds(475, 390, 117, 29);
        frame.getContentPane().add(battleButton);

        JButton cancelButton = new JButton("Return");
        cancelButton.setBounds(187, 390, 117, 29);
        frame.getContentPane().add(cancelButton);


        selectedTrainer.onChange(panel::refresh);

        enemiesComboBox.addActionListener(selectEnemyAction(enemiesComboBox));
        battleButton.addActionListener(battleAction(enemiesComboBox));
        cancelButton.addActionListener(backToMainMenuAction());

        frame.setVisible(true);
    }

    /**
     * The action performed when a new trainer is selected
     *
     * @param enemiesComboBox The combobox to get the new selection
     * @return An action listener for the combo-box
     */
    private ActionListener selectEnemyAction(JComboBox<String> enemiesComboBox) {
        return ignoredEvent -> {
            final var index = enemiesComboBox.getSelectedIndex();
            if (index < 0 || index >= gameManager.getAvailableBattles().size())
                return;
            final var newEnemy = gameManager.getAvailableBattles().get(index);
            selectedTrainer.set(newEnemy);
        };
    }

    /**
     * The action performed when starting the battle
     *
     * @param enemiesComboBox The combobox to get the new selection
     * @return An action listener for the battle button
     */
    private ActionListener battleAction(JComboBox<String> enemiesComboBox) {
        return ignoredEvent -> {
            final var index = enemiesComboBox.getSelectedIndex();
            gui.navigateTo(new BattleScreen(gui, gameManager, index));
        };
    }

    /**
     * The action performed when going back to the main menu
     *
     * @return An action listener for the return button
     */
    private ActionListener backToMainMenuAction() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }
}
