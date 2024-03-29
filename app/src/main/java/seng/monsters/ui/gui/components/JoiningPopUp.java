package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;
import seng.monsters.ui.gui.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Monster joining party pop up screen that prompts the user to rename their monster
 * <p>
 * This is not a Screen and not replaced the current active window
 */
public class JoiningPopUp extends PopUp {
    /**
     * Monster that has joined
     */
    private final Monster monster;

    /**
     * The ending callback
     */
    private Consumer<ActionEvent> onEnd = ignoredEvent -> {
    };

    /**
     * Create a pop-up screen to allow the player to name their new monster
     *
     * @param monster The monster that have just joined
     */
    public JoiningPopUp(Monster monster) {
        this.monster = monster;
        render();
    }

    private void render() {
        // Title label using the name of the monster
        JLabel titleLabel = new JLabel(
            String.format("%s has joined your party!", monster.getName())
        );
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 26));
        titleLabel.setBounds(179, 52, 460, 39);
        frame.getContentPane().add(titleLabel);

        // Prompt label to ask for renaming the monster
        JLabel promptLabel = new JLabel("Name your monster:");
        promptLabel.setHorizontalAlignment(SwingConstants.CENTER);
        promptLabel.setBounds(125, 160, 229, 16);
        frame.getContentPane().add(promptLabel);

        // Textfield to renaming with the current name as text
        JTextField textField = new JTextField();
        textField.setToolTipText("Must be 3 to 15 characters");
        textField.setText(monster.getName());
        textField.setBounds(125, 195, 229, 26);
        frame.getContentPane().add(textField);
        textField.setColumns(10);

        // The icon for the monster joining
        JLabel iconLabel = new JLabel();
        Screen.imageIconFromResource(String.format("/images/%s.gif", monster.monsterType().toLowerCase()))
            .ifPresent(iconLabel::setIcon);
        iconLabel.setBounds(475, 160, 700, 171);
        frame.getContentPane().add(iconLabel);

        // The button to apply the name change and trigger the onEnd callback
        JButton doneButton = new JButton("Accept");
        doneButton.setBounds(351, 400, 117, 29);
        frame.getContentPane().add(doneButton);

        // The error label for the name
        JLabel errorLabel = new JLabel();
        errorLabel.setForeground(new Color(255, 0, 0));
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        errorLabel.setBounds(125, 226, 229, 32);
        errorLabel.setVisible(false);
        frame.getContentPane().add(errorLabel);

        doneButton.addActionListener(doneAction(textField, errorLabel));

        frame.setVisible(true);
    }

    /**
     * Added an <code>onEnd</code> callback to be run when the <code>done</code> button is pressed.
     *
     * @param callback The callback to be called
     */
    public void onEnd(Consumer<ActionEvent> callback) {
        this.onEnd = callback;
    }

    /**
     * The action performed when the user has chosen a name for the monster
     *
     * @param textField  The text field input
     * @param errorLabel The error label to display error in input
     * @return An action listener for the done button
     */
    private ActionListener doneAction(JTextField textField, JLabel errorLabel) {
        return ignoredEvent -> {
            final String input = textField.getText();
            try {
                monster.setName(input);
                onEnd.accept(ignoredEvent);
                frame.dispose();
            } catch (IllegalArgumentException ignored) {
                errorLabel.setText("<html>Must be 3 to 15 characters and only contain letters!</html>");
                errorLabel.setVisible(true);
            }
        };
    }
}
