package seng.monsters.ui.gui;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import seng.monsters.model.GameManager;
import seng.monsters.model.Trainer;
import seng.monsters.ui.gui.components.PartyPanel;
import seng.monsters.ui.gui.state.LabelComboboxModel;
import seng.monsters.ui.gui.state.State;

import javax.swing.SwingConstants;

public class AvailableBattlesScreen extends Screen {
	
	private final State<Trainer> selectedTrainer;
	
	/**
	 * Create the application.
	 */
	public AvailableBattlesScreen(GUI gui, GameManager gameManager) {
		super(gui, gameManager);
		selectedTrainer = State.of(gameManager.getAvailableBattles().get(0));
	}

	public void render() {
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

		JButton battleButton = new JButton("Next");
		battleButton.setBounds(187, 390, 117, 29);
		frame.getContentPane().add(battleButton);
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBounds(475, 390, 117, 29);
		frame.getContentPane().add(cancelButton);
        
		
		selectedTrainer.onChange(panel::refresh);
		
		enemiesComboBox.addActionListener(selectEnemyAction(enemiesComboBox));
		battleButton.addActionListener(battleAction(enemiesComboBox));
		cancelButton.addActionListener(backToMainMenuAction());
        
        	frame.setVisible(true);
    }
	
	private ActionListener selectEnemyAction(JComboBox<String> enemiesComboBox) {
		return e -> {
			final var index = enemiesComboBox.getSelectedIndex();
			final var newEnemy = gameManager.getAvailableBattles().get(index);
			selectedTrainer.set(newEnemy);
		};
	}
	
	private ActionListener battleAction(JComboBox<String> enemiesComboBox) {
		return e -> {
			final var index = enemiesComboBox.getSelectedIndex();
			gui.navigateTo(new BattleScreen(gui, gameManager, index));
		};
	}
	
	private ActionListener backToMainMenuAction() {
		return e -> gui.navigateBackToMainMenu();
	}
}
