package seng.monsters.ui.gui;

import java.awt.Color;

import javax.swing.JFrame;

import seng.monsters.model.Monster;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class MonsterJoiningPopUp {

	private JFrame frame;
	private final Monster monster;
	private Consumer<ActionEvent> onEnd = e -> {};
	private JTextField textField;

	/**
	 * Create the application.
	 */
	public MonsterJoiningPopUp(Monster monster) {
		this.monster = monster;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(new Color(255, 255, 204));
		frame.getContentPane().setForeground(new Color(0, 0, 0));
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 660, 399);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel titleLabel = new JLabel(
			String.format("%s has joined your party!", monster.getName())
		);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
		titleLabel.setBounds(100, 58, 460, 39);
		frame.getContentPane().add(titleLabel);
		
		JLabel promptLabel = new JLabel("Name your monster:");
		promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
		promptLabel.setBounds(100, 128, 229, 16);
		frame.getContentPane().add(promptLabel);
		
		textField = new JTextField();
		textField.setToolTipText("Must be 3 to 15 characters");
		textField.setText(monster.getName());
		textField.setBounds(100, 156, 229, 26);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel iconLabel = new JLabel("");
		iconLabel.setIcon(new ImageIcon(
			MonsterJoiningPopUp.class.getResource(
				String.format("/images/%s.gif", monster.monsterType())
			)
		));
		iconLabel.setBounds(380, 128, 146, 171);
		frame.getContentPane().add(iconLabel);
		
		JButton doneButton = new JButton("Apply");
		doneButton.setBounds(281, 320, 117, 29);
		frame.getContentPane().add(doneButton);
		
		JLabel errorLabel = new JLabel("Name your monster:");
		errorLabel.setForeground(new Color(255, 0, 0));
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		errorLabel.setBounds(100, 197, 229, 16);
		errorLabel.setVisible(false);
		frame.getContentPane().add(errorLabel);
		
		doneButton.addActionListener(e -> {
			final var input = textField.getText();
			
			if ((input.length() < 3) || (input.length() > 15) || (!input.matches("[a-zA-Z]+"))) {
				errorLabel.setText("Must be 3 to 15 characters!");
				errorLabel.setVisible(true);
				return;
			}
			
			monster.setName(input);
			onEnd.accept(e);
			frame.dispose();
		});
		
		frame.setVisible(true);
	}
	
	public void onEnd(Consumer<ActionEvent> callback) {
		this.onEnd = callback;
	}
}
