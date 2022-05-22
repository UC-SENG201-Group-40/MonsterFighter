package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.ui.gui.components.JoiningPopUp;
import seng.monsters.ui.gui.components.LeavingPopUp;
import seng.monsters.ui.gui.components.LevelledUpPopUp;
import seng.monsters.ui.gui.components.SelectShopPopUp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Optional;
import java.util.List;

/**
 * A screen to show all the current day properties, navigate to party, inventory, and shop, select a battle, and sleep
 */
public class MainMenuScreen extends Screen {
    /**
     * Create an active GUI screen for displaying the main menu
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public MainMenuScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
    }

    @Override
    public void render() {
        // Changes background to the current environment
        Screen
            .imageIconFromResource(
                String.format("/images/%s.jpeg", gameManager.getEnvironment().toString().toLowerCase())
            )
            .ifPresent(icon -> frame.setContentPane(new JLabel(icon)));

        // Button to go to the party screen
        JButton partyButton = new JButton("Party");
        partyButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        partyButton.setBounds(38, 45, 150, 40);
        partyButton.setEnabled(!gameManager.getPlayer().getParty().isEmpty());
        frame.getContentPane().add(partyButton);

        // Button to go to the inventory screen
        JButton inventoryButton = new JButton("Inventory");
        inventoryButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        inventoryButton.setBounds(631, 45, 150, 40);
        frame.getContentPane().add(inventoryButton);

        // Button to go to the shop screen
        JButton shopButton = new JButton("Shop");
        shopButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        shopButton.setBounds(38, 320, 150, 40);
        frame.getContentPane().add(shopButton);

        // Button to go to the sleep screen
        JButton sleepButton = new JButton("Sleep");
        sleepButton.setFont(new Font("Lucida Grande", Font.PLAIN, 15));
        sleepButton.setBounds(631, 320, 150, 40);
        sleepButton.setEnabled(
            gameManager.hasNoPossibilityForRevive()
                || gameManager.hasNotEnoughMoneyForMonster()
                || !gameManager.hasNotBattleOnce()
        );
        frame.getContentPane().add(sleepButton);

        // Label for when an error occurs
        JLabel errorLabel = new JLabel();
        errorLabel.setBounds(200, 484, 419, 16);
        errorLabel.setVisible(false);
        errorLabel.setForeground(Color.RED);
        frame.getContentPane().add(errorLabel);

        // Panel displaying the game's info
        JPanel gameInfoPanel = new JPanel();
        gameInfoPanel.setBackground(Color.WHITE);
        gameInfoPanel.setBounds(200, 45, 419, 115);
        frame.getContentPane().add(gameInfoPanel);
        gameInfoPanel.setLayout(null);

        JLabel dayLabel = new JLabel(
            String.format("Day %d/%d", gameManager.getCurrentDay(), gameManager.getMaxDays())
        );
        dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dayLabel.setBounds(6, 6, 407, 16);
        gameInfoPanel.add(dayLabel);

        JPanel dayPanel = new JPanel();
        dayPanel.setBackground(new Color(127, 255, 212));
        dayPanel.setBounds(6, 6, 407 * gameManager.getCurrentDay() / gameManager.getMaxDays(), 18);
        gameInfoPanel.add(dayPanel);

        JPanel maxDayPanel = new JPanel();
        maxDayPanel.setBackground(new Color(230, 230, 250));
        maxDayPanel.setBounds(6, 6, 407, 18);
        gameInfoPanel.add(maxDayPanel);

        JLabel goldLabel = new JLabel(
            String.format("Gold reserve: %d", gameManager.getGold())
        );
        goldLabel.setForeground(new Color(0, 0, 0));
        goldLabel.setBounds(6, 36, 407, 16);
        gameInfoPanel.add(goldLabel);

        JLabel scoreLabel = new JLabel(
            String.format("Score: %d", gameManager.getScore())
        );
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(6, 64, 407, 16);
        gameInfoPanel.add(scoreLabel);

        JLabel environmentLabel = new JLabel(
            String.format("Environment: %s", gameManager.getEnvironment().toString())
        );
        environmentLabel.setForeground(Color.BLACK);
        environmentLabel.setBounds(6, 92, 407, 16);
        gameInfoPanel.add(environmentLabel);

        // Button to go to the available battles screen
        JButton battlesButton = new JButton("Battles");
        battlesButton.setForeground(new Color(220, 20, 60));
        battlesButton.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
        battlesButton.setBounds(309, 320, 200, 61);
        battlesButton.setEnabled(
            !gameManager.getAvailableBattles().isEmpty()
                && !gameManager.getPlayer().isWhitedOut()
        );
        frame.getContentPane().add(battlesButton);

        partyButton.addActionListener(managePartyAction());
        inventoryButton.addActionListener(manageInventoryAction());
        sleepButton.addActionListener(sleepAction(errorLabel));
        battlesButton.addActionListener(lookAvailableBattlesAction());
        shopButton.addActionListener(ignored -> new SelectShopPopUp(gui, gameManager));

        frame.setResizable(false);
        frame.setVisible(true);
    }

    private ActionListener lookAvailableBattlesAction() {
        return ignoredEvent -> gui.navigateTo(new AvailableBattlesScreen(gui, gameManager));
    }

    /**
     * The action performed when the party button is used (navigates to the party screen)
     *
     * @return An action listener for the party button
     */
    private ActionListener managePartyAction() {
        return ignoredEvent -> gui.navigateTo(new PartyScreen(gui, gameManager));
    }

    /**
     * The action performed when the inventory button is used (navigates to the inventory screen)
     *
     * @return An action listener for the inventory button
     */
    private ActionListener manageInventoryAction() {
        return ignoredEvent -> gui.navigateTo(new InventoryScreen(gui, gameManager));
    }

    /**
     * The action performed when the sleep button is used (sleeps, cycles to the next day, and triggers night events)
     *
     * @param errorLabel The error label to display failure to proceed
     * @return An action listener for the sleep button
     */
    private ActionListener sleepAction(JLabel errorLabel) {
        return ignoredEvent -> {
            // Can't sleep if the player hasn't battled but is capable of doing so
            if (!gameManager.getPlayer().isWhitedOut() && gameManager.hasNotBattleOnce()) {
                errorLabel.setVisible(true);
                errorLabel.setText("You must battle at least once before you can sleep!");
                return;
            }

            final boolean isEnded = gameManager.nextDay();

            // Game ends and goes to the end screen if the game has finished
            if (isEnded) {
                gui.navigateTo(new EndScreen(gui, gameManager, gameManager.getCurrentDay() > gameManager.getMaxDays()));
                return;
            }

            // Else the game returns to the main menu and displays any popups if any night events were triggered
            gui.navigateBackToMainMenu();

            final Optional<Monster> maybeLeaving = gameManager.partyMonstersLeave();
            maybeLeaving.ifPresent(LeavingPopUp::new);

            final List<Monster> levelledUp = gameManager.partyMonstersLevelUp();
            if (!levelledUp.isEmpty())
                new LevelledUpPopUp(levelledUp);

            final Optional<Monster> maybeJoining = gameManager.monsterJoinsParty();
            maybeJoining.ifPresent(JoiningPopUp::new);
        };
    }
}
