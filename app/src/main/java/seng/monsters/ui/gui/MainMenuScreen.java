package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.ui.gui.components.JoiningPopUp;
import seng.monsters.ui.gui.components.LeavingPopUp;
import seng.monsters.ui.gui.components.LevelledUpPopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * A screen to show all the current day properties, navigate to party, inventory, and shop, select a battle, and sleep
 */
public class MainMenuScreen extends Screen {
    /**
     * Create the application.
     */
    public MainMenuScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
    }

    @Override
    public void render() {
        frame.setContentPane(new JLabel(
            new ImageIcon(
                Objects.requireNonNull(BattleScreen.class.getResource(
                    String.format("/images/%s.jpeg", gameManager.getEnvironment().toString())
                )))
        ));

        JButton partyButton = new JButton("Party");
        partyButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        partyButton.setBounds(38, 45, 150, 40);
        frame.getContentPane().add(partyButton);

        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        inventoryButton.setBounds(631, 45, 150, 40);
        frame.getContentPane().add(inventoryButton);

        JButton shopButton = new JButton("Shop");
        shopButton.setEnabled(false);
        shopButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        shopButton.setBounds(38, 320, 150, 40);
        frame.getContentPane().add(shopButton);

        JButton sleepButton = new JButton("Sleep");
        sleepButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        sleepButton.setBounds(631, 320, 150, 40);
        frame.getContentPane().add(sleepButton);

        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(200, 484, 419, 16);
        errorLabel.setVisible(false);
        errorLabel.setForeground(Color.RED);
        frame.getContentPane().add(errorLabel);

        JPanel displayPanel = new JPanel();
        displayPanel.setBackground(Color.WHITE);
        displayPanel.setBounds(200, 45, 419, 115);
        frame.getContentPane().add(displayPanel);
        displayPanel.setLayout(null);

        JLabel dayLabel = new JLabel(
            String.format("Day %d/%d", gameManager.getCurrentDay(), gameManager.getMaxDays())
        );
        dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dayLabel.setBounds(6, 6, 407, 16);
        displayPanel.add(dayLabel);

        JPanel dayPanel = new JPanel();
        dayPanel.setBackground(new Color(127, 255, 212));
        dayPanel.setBounds(6, 6, 407 * gameManager.getCurrentDay() / gameManager.getMaxDays(), 18);
        displayPanel.add(dayPanel);

        JPanel maxDayPanel = new JPanel();
        maxDayPanel.setBackground(new Color(230, 230, 250));
        maxDayPanel.setBounds(6, 6, 407, 18);
        displayPanel.add(maxDayPanel);

        JLabel goldLabel = new JLabel(
            String.format("Gold reserve: %d", gameManager.getGold())
        );
        goldLabel.setForeground(new Color(0, 0, 0));
        goldLabel.setBounds(6, 36, 407, 16);
        displayPanel.add(goldLabel);

        JLabel scoreLabel = new JLabel(
            String.format("Score: %d", gameManager.getScore())
        );
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(6, 64, 407, 16);
        displayPanel.add(scoreLabel);

        JLabel environmentLabel = new JLabel(
            String.format("Environment: %s", gameManager.getEnvironment().toString())
        );
        environmentLabel.setForeground(Color.BLACK);
        environmentLabel.setBounds(6, 92, 407, 16);
        displayPanel.add(environmentLabel);

        JButton battlesButton = new JButton("Battles");
        battlesButton.setForeground(new Color(220, 20, 60));
        battlesButton.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
        battlesButton.setBounds(309, 320, 200, 61);
        battlesButton.setEnabled(!gameManager.getAvailableBattles().isEmpty());
        frame.getContentPane().add(battlesButton);

        partyButton.addActionListener(managePartyAction());
        inventoryButton.addActionListener(manageInventoryAction());
        sleepButton.addActionListener(sleepAction(errorLabel));
        battlesButton.addActionListener(lookAvailableBattlesAction());

        frame.setResizable(false);
        frame.setVisible(true);
    }

    private ActionListener lookAvailableBattlesAction() {
        return e -> gui.navigateTo(new AvailableBattlesScreen(gui, gameManager));
    }

    /**
     * The action performed to navigate to party
     *
     * @return An action listener for the party button
     */
    private ActionListener managePartyAction() {
        return e -> gui.navigateTo(new PartyScreen(gui, gameManager));
    }

    /**
     * The action performed to navigate to inventory
     *
     * @return An action listener for the inventory button
     */
    private ActionListener manageInventoryAction() {
        return e -> gui.navigateTo(new InventoryScreen(gui, gameManager));
    }

    /**
     * The action performed to sleep and go to the next day
     *
     * @param errorLabel The error label to display failure to proceed
     * @return An action listener for the sleep button
     */
    private ActionListener sleepAction(JLabel errorLabel) {
        return e -> {
            if (gameManager.hasNotBattleOnce()) {
                errorLabel.setVisible(true);
                errorLabel.setText("You can sleep and go to the next sinc you haven't battle once for the day");
                return;
            }

            final var isEnded = gameManager.nextDay();

            if (isEnded) {
                gui.navigateTo(new EndScreen(gui, gameManager));
                return;
            }

            gui.navigateBackToMainMenu();

            final var maybeLeaving = gameManager.partyMonstersLeave();
            maybeLeaving.ifPresent(LeavingPopUp::new);

            final var levelledUp = gameManager.partyMonstersLevelUp();
            if (!levelledUp.isEmpty())
                new LevelledUpPopUp(levelledUp);

            final var maybeJoining = gameManager.monsterJoinsParty();
            maybeJoining.ifPresent(JoiningPopUp::new);
        };
    }
}
