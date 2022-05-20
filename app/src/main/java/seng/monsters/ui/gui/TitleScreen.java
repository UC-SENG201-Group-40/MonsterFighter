package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * A screen as part of the setup process to prompt user with their name
 */
public class TitleScreen extends Screen {

    /**
     * Create an active GUI screen for the title and starting the game
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public TitleScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
    }

    @Override
    public void render() {
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

        JTextField nameTextField = new JTextField();
        nameTextField.setToolTipText("Must be between 3 to 15 characters");
        nameTextField.setBounds(326, 315, 167, 26);
        frame.getContentPane().add(nameTextField);
        nameTextField.setColumns(10);

        JButton submitButton = new JButton("Next");
        submitButton.setBounds(350, 374, 117, 29);
        frame.getContentPane().add(submitButton);

        JLabel errorLabel = new JLabel("");
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 12));
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setBounds(6, 346, 807, 16);
        errorLabel.setVisible(false);
        frame.getContentPane().add(errorLabel);

        submitButton.addActionListener(
            submitAction(nameTextField, errorLabel)
        );

        frame.setVisible(true);
    }

    /**
     * The action when the user submitted their name
     *
     * @param textField  The text field to get input
     * @param errorLabel The error label to display error in the name input
     * @return An action listener for the submit button
     */
    private ActionListener submitAction(JTextField textField, JLabel errorLabel) {
        return ignoredEvent -> {
            final String input = textField.getText();
            try {
                gameManager.setTrainerName(input);
                gui.navigateTo(new SettingsScreen(gui, gameManager));
            } catch (IllegalArgumentException ignored) {
                errorLabel.setVisible(true);
                errorLabel.setText("<html>Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)</html>");
            }
        };
    }
}
