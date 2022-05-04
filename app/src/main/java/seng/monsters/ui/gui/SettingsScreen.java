package seng.monsters.ui.gui;

import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import seng.monsters.model.GameManager;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.SwingConstants;

public class SettingsScreen implements Screen {
	private final GUI gui;
	private final GameManager gameManager;
	
	private int difficulty = 1;
	private int maxDays = 5;
	private JFrame frame;
	private JTextField maxDaysTextField;
	private JLabel difficultyLabel;
	private JLabel maxDaysLabel;

	/**
	 * Create the application.
	 */
	public SettingsScreen(GUI gui, GameManager gameManager) {
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
		
		JLabel difficultyPromptLabel = new JLabel("Choose difficulty:");
		difficultyPromptLabel.setBounds(145, 68, 153, 16);
		frame.getContentPane().add(difficultyPromptLabel);
		
		JLabel maxDaysPromptLabel = new JLabel("Choose number of days to play:");
		maxDaysPromptLabel.setBounds(145, 193, 220, 16);
		frame.getContentPane().add(maxDaysPromptLabel);
		
		difficultyLabel = new JLabel("");
		difficultyLabel.setBounds(400, 68, 346, 16);
		frame.getContentPane().add(difficultyLabel);
		
		maxDaysLabel = new JLabel("");
		maxDaysLabel.setBounds(400, 193, 346, 16);
		frame.getContentPane().add(maxDaysLabel);
		
		JButton normalDifficultyButton = new JButton("Normal");
		normalDifficultyButton.setBounds(181, 112, 117, 29);
		frame.getContentPane().add(normalDifficultyButton);
		
		JButton hardDifficultButton = new JButton("Hard");
		hardDifficultButton.setBounds(324, 112, 117, 29);
		frame.getContentPane().add(hardDifficultButton);
		
		JButton impossibleDifficultyLabel = new JButton("Impossible");
		impossibleDifficultyLabel.setBounds(473, 112, 117, 29);
		frame.getContentPane().add(impossibleDifficultyLabel);
		
		maxDaysTextField = new JTextField();
		maxDaysTextField.setToolTipText("Must be between 5 to 15 days");
		maxDaysTextField.setText("5");
		maxDaysTextField.setBounds(181, 242, 263, 26);
		frame.getContentPane().add(maxDaysTextField);
		maxDaysTextField.setColumns(10);
		
		JButton verifyMaxDaysButton = new JButton("Change");
		verifyMaxDaysButton.setBounds(473, 242, 117, 29);
		frame.getContentPane().add(verifyMaxDaysButton);
		
		JButton submitButton = new JButton("Next");
		submitButton.setBounds(350, 374, 117, 29);
		frame.getContentPane().add(submitButton);
		
		JLabel errorLabel = new JLabel("");
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setForeground(new Color(255, 0, 0));
		errorLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
		errorLabel.setBounds(6, 340, 807, 16);
		errorLabel.setVisible(false);
		frame.getContentPane().add(errorLabel);
		
		hardDifficultButton.addActionListener(e -> {
			difficulty = 2;
			refresh();
		});
		impossibleDifficultyLabel.addActionListener(e -> {
			difficulty = 3;
			refresh();
		});
		normalDifficultyButton.addActionListener(e -> {
			difficulty = 1;
			refresh();
		});
		
		verifyMaxDaysButton.addActionListener(e -> {
			final var input = maxDaysTextField.getText();
			try {
				final var maxDaysInput = Integer.decode(input);
				if (maxDaysInput < 5 || maxDaysInput > 15) 
					throw new NumberFormatException();
				maxDays = maxDaysInput;
				refresh();
				errorLabel.setVisible(false);
			} catch (NumberFormatException ignored) {
				errorLabel.setVisible(true);
				errorLabel.setText("Invalid number of max days! (Must be a number between 5 and 15 inclusive)");
			}
		});
		
		submitButton.addActionListener(e -> {
			gameManager.setMaxDays(maxDays);
			gameManager.setDifficulty(difficulty);
			gameManager.refreshCurrentDay();
			
			gui.navigateTo(new StartingMonsterScreen(gui, gameManager));
		});
		
		frame.setVisible(true);
		refresh();
	}
	
	@Override
	public void dispose() {
		frame.dispose();
	}
	
	private void refresh() {
		final var difficulties = List.of("Normal", "Hard", "Impossible");
		difficultyLabel.setText(difficulties.get(difficulty - 1));
		
		maxDaysLabel.setText(String.format("%d day(s)", maxDays));
		maxDaysTextField.setText("");
	}

}
