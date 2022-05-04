package seng.monsters.ui.gui;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class StartingMonsterScreen implements Screen {
	
	private final List<Monster> startingMonsters = Monster.all(1).subList(0, 3);
	
	private final GUI gui;
	private final GameManager gameManager;
	
	private JFrame frame;
	private JComboBox<String> startingMonsterComboBox;
	private DetailedMonsterPanel panel;


	/**
	 * Create the application.
	 */
	public StartingMonsterScreen(GUI gui, GameManager gameManager) {
		this.gui = gui;
		this.gameManager = gameManager;
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 204));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 819, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		startingMonsterComboBox = new JComboBox<>();
		startingMonsterComboBox.setModel(
			new DefaultComboBoxModel<String>(startingMonsters
				.stream()
				.map(Monster::monsterType)
				.toList()
				.toArray(new String[startingMonsters.size()])
			)
		);
		startingMonsterComboBox.setSelectedIndex(0);
		startingMonsterComboBox.setBounds(66, 140, 238, 27);
		frame.getContentPane().add(startingMonsterComboBox);
		
		JLabel startingMonsterPrompt = new JLabel("Choose your starting monster:");
		startingMonsterPrompt.setBounds(66, 112, 238, 16);
		frame.getContentPane().add(startingMonsterPrompt);
		
		panel = new DetailedMonsterPanel(chosenMonster(), false);
		panel.setBounds(439, 90, 300, 251);
		panel.applyToFrame(frame);
		
		JButton submitButton = new JButton("Next");
		submitButton.setBounds(350, 374, 117, 29);
		frame.getContentPane().add(submitButton);
		
		startingMonsterComboBox.addActionListener(e -> {
			if (startingMonsterComboBox.getSelectedIndex() < 0) 
				return;
			refresh();
		});
		
		submitButton.addActionListener(e -> {
			final var monster = chosenMonster();
			gameManager.getTrainer().add(monster);
			startingMonsterComboBox.setEnabled(false);
			
			final var popUp = new MonsterJoiningPopUp(monster);
			popUp.onEnd(ev -> {
				
				// TODO: Navigate to MainMenu 
				System.out.printf("Name: %s\n", gameManager.getTrainer().getName());
				for (final var mon : gameManager.getTrainer().getParty()) {
					System.out.printf("- %s (%s) lv %d\n", mon.getName(), mon.monsterType(), mon.getLevel());
				}
				System.out.printf("Difficulty: %d, Days: %d\n", gameManager.getDifficulty(), gameManager.getMaxDays());
				gui.quit();
			});
		});
		
		frame.setVisible(true);
	}
	
	@Override
	public void dispose() {
		frame.dispose();
	}
	
	private void refresh() {
		panel.refresh(chosenMonster());
	}
	
	private Monster chosenMonster() {
		return startingMonsters.get(startingMonsterComboBox.getSelectedIndex());
	}
}
