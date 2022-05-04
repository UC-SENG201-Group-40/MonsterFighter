package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;

import javax.swing.*;
import java.awt.*;

/**
 * A screen as part of the setup process to prompt user with their name
 */
public class TitleScreen implements Screen {

	/**
	 * The GUi for navigation
	 */
	private final GUI gui;

	/**
	 * The game manager as the model
	 */
	private final GameManager gameManager;

	/**
	 * The window frame
	 */
	private JFrame frame;

    /**
     * Create the application.
     */
    public TitleScreen(GUI gui, GameManager gameManager) {
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

        submitButton.addActionListener(e -> {
            final var input = nameTextField.getText();
            if ((input.length() < 3) || (input.length() > 15) || (!input.matches("[a-zA-Z]+"))) {
                errorLabel.setVisible(true);
                errorLabel.setText("Invalid name! (Must be between 3 and 15 letters inclusive, no symbols or numbers)");
                return;
            }
            gameManager.setTrainerName(input);
            gui.navigateTo(new SettingsScreen(gui, gameManager));
        });

        frame.setVisible(true);
    }

    @Override
    public void dispose() {
        frame.dispose();
    }
}
