package seng.monsters.ui.gui;

import javax.swing.JFrame;

import seng.monsters.model.GameManager;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;

public class TitleScreen implements Screen {

	private final GUI gui;
	private final GameManager gameManager;
	
	private JFrame frame;
	private JTextField nameTextField;

	/**
	 * Create the application.
	 */
	public TitleScreen(GUI gui, GameManager gameManager) {
		this.gui = gui;
		this.gameManager = gameManager;
	}

	@Override
	public void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 204));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.setBounds(100, 100, 819, 487);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel titleLabel = new JLabel("Monster Fighter");
		titleLabel.setFont(new Font("Lucida Grande", Font.BOLD, 36));
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setBounds(229, 94, 360, 67);
		frame.getContentPane().add(titleLabel);
		
		JLabel subtitleLabel = new JLabel("Made by David Liang and Vincent");
		subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		subtitleLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 13));
		subtitleLabel.setBounds(305, 185, 208, 16);
		frame.getContentPane().add(subtitleLabel);
		
		JLabel namePromptLabel = new JLabel("Choose a name:");
		namePromptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		namePromptLabel.setBounds(326, 287, 167, 16);
		frame.getContentPane().add(namePromptLabel);
		
		nameTextField = new JTextField();
		nameTextField.setToolTipText("Must be between 3 to 15 characters");
		nameTextField.setBounds(326, 315, 167, 26);
		frame.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		JButton submitButton = new JButton("Next");
		submitButton.setBounds(350, 374, 117, 29);
		frame.getContentPane().add(submitButton);
		
		
		submitButton.addActionListener(e -> {
			final var input = nameTextField.getText();
			gameManager.setTrainerName(input);
			
			gui.navigateTo(new TitleScreen(gui, gameManager));
		});
		
		frame.setVisible(true);
	}
	
	@Override
	public void dispose() {
		frame.dispose();
	}
}
