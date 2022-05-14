package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.ui.gui.state.State;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * A screen as part of the setup process to prompt user to select difficulty and choose the amount of days for the game
 */
public class SettingsScreen extends Screen {
    /**
     * The difficulty local state before applied to model
     */
    private final State<Integer> difficulty = State.of(1);

    /**
     * The max days local state before applied to the model
     */
    private final State<Integer> maxDays = State.of(5);

    /**
     * Create an active GUI screen for prompting the user to choose the settings before the game starts
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public SettingsScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
    }

    @Override
    public void render() {
        JLabel difficultyPromptLabel = new JLabel("Choose difficulty:");
        difficultyPromptLabel.setBounds(145, 68, 153, 16);
        frame.getContentPane().add(difficultyPromptLabel);

        JLabel maxDaysPromptLabel = new JLabel("Choose number of days to play:");
        maxDaysPromptLabel.setBounds(145, 193, 220, 16);
        frame.getContentPane().add(maxDaysPromptLabel);

        JLabel difficultyLabel = new JLabel("Normal");
        difficultyLabel.setBounds(400, 68, 346, 16);
        frame.getContentPane().add(difficultyLabel);

        JLabel maxDaysLabel = new JLabel("5 day(s)");
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

        JTextField maxDaysTextField = new JTextField();
        maxDaysTextField.setToolTipText("Must be between 5 to 15 days");
        maxDaysTextField.setText(maxDays.get().toString());
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

        // Setting the on change callback for the difficulty
        difficulty.onChange(currDifficulty -> {
            final var difficulties = List.of("Normal", "Hard", "Impossible");
            difficultyLabel.setText(difficulties.get(currDifficulty - 1));
        });

        // Setting the on change callback for the max days
        maxDays.onChange(currMaxDays -> {
            maxDaysLabel.setText(String.format("%d day(s)", currMaxDays));
            maxDaysTextField.setText("");
        });

        hardDifficultButton.addActionListener(ignoredEvent -> difficulty.set(2));
        impossibleDifficultyLabel.addActionListener(ignoredEvent -> difficulty.set(3));
        normalDifficultyButton.addActionListener(ignoredEvent -> difficulty.set(1));

        verifyMaxDaysButton.addActionListener(
            verifyMaxDaysAction(maxDaysTextField, errorLabel)
        );

        submitButton.addActionListener(submitAction());

        frame.setVisible(true);
    }

    /**
     * The action performed when verifying the max days input
     *
     * @param maxDaysTextField The text-field to get the text input
     * @param errorLabel       The error label to display error in input
     * @return The action listener for the verify button
     */
    private ActionListener verifyMaxDaysAction(JTextField maxDaysTextField, JLabel errorLabel) {
        return ignoredEvent -> {
            final var input = maxDaysTextField.getText();
            try {
                final int maxDaysInput = Integer.decode(input);
                if (maxDaysInput < 5 || maxDaysInput > 15)
                    throw new NumberFormatException();
                maxDays.set(maxDaysInput);
                errorLabel.setVisible(false);
            } catch (NumberFormatException ignored) {
                errorLabel.setVisible(true);
                errorLabel.setText("Invalid number of max days! (Must be a number between 5 and 15 inclusive)");
            }
        };
    }

    /**
     * The action performed when user submitted the settings
     *
     * @return The action listener for the submit button
     */
    private ActionListener submitAction() {
        return ignoredEvent -> {
            gameManager.setMaxDays(maxDays.get());
            gameManager.setDifficulty(difficulty.get());
            gameManager.refreshCurrentDay();

            gui.navigateTo(new StartingMonsterScreen(gui, gameManager));
        };
    }
}
