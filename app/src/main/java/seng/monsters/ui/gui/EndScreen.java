package seng.monsters.ui.gui;

import java.awt.Color;

import javax.swing.*;
import java.awt.Font;
import java.awt.event.ActionListener;

import seng.monsters.model.GameManager;
import seng.monsters.ui.gui.components.PartyPanel;

/**
 * A screen to display end game result
 */
public class EndScreen extends Screen {

    /**
     * Signal if the end screen should be congratulating the player or not
     */
    private final boolean isSuccessful;

    /**
     * Create an active GUI screen for displaying the end game report
     *
     * @param gui         The GUI manager
     * @param gameManager The Game logic manager / controller
     * @param isSuccessful Signal if the game ended due to a victory or a defeat
     */
    public EndScreen(GUI gui, GameManager gameManager, boolean isSuccessful) {
        super(gui, gameManager);
        this.isSuccessful = isSuccessful;
    }

    @Override
    public void render() {
        // Title label announcing the player's success or failure
        JLabel titleLabel = new JLabel(String.format(
            isSuccessful
                ? "Well done, %s! You completed the game!"
                : "Too bad, %s! You lost the game",
            gameManager.getPlayer().getName()
        ));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 24));
        titleLabel.setBounds(6, 25, 807, 39);
        frame.getContentPane().add(titleLabel);

        // Panel for end game report
        JPanel gameReportPanel = new JPanel();
        gameReportPanel.setBounds(113, 115, 212, 234);
        frame.getContentPane().add(gameReportPanel);
        gameReportPanel.setLayout(null);

        JLabel gameReportLabel = new JLabel("Here is your game report:");
        gameReportLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        gameReportLabel.setBounds(6, 6, 200, 16);
        gameReportPanel.add(gameReportLabel);

        JLabel dayReachLabel = new JLabel("Day(s) reached:");
        dayReachLabel.setBounds(6, 57, 200, 16);
        gameReportPanel.add(dayReachLabel);

        JLabel goldCollectedLabel = new JLabel("Total gold collected:");
        goldCollectedLabel.setBounds(6, 108, 200, 16);
        gameReportPanel.add(goldCollectedLabel);

        JLabel diificultyLabel = new JLabel(String.format(
            "Difficulty: %s", difficultyName()
        ));
        diificultyLabel.setBounds(6, 34, 200, 16);
        gameReportPanel.add(diificultyLabel);

        JLabel scoreAchievedLabel = new JLabel("Total score achieved:");
        scoreAchievedLabel.setBounds(6, 159, 200, 16);
        gameReportPanel.add(scoreAchievedLabel);

        JLabel dayProgressLabel = new JLabel(String.format(
            "%d/%d",
            gameManager.getCurrentDay() - 1,
            gameManager.getMaxDays()
        ));
        dayProgressLabel.setForeground(color(gameManager.getCurrentDay(), gameManager.getMaxDays()));
        dayProgressLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dayProgressLabel.setBounds(6, 80, 200, 16);
        gameReportPanel.add(dayProgressLabel);

        JLabel goldLabel = new JLabel(String.format("%d gold", gameManager.getGold()));
        goldLabel.setHorizontalAlignment(SwingConstants.CENTER);
        goldLabel.setForeground(new Color(0, 0, 0));
        goldLabel.setBounds(6, 136, 200, 16);
        gameReportPanel.add(goldLabel);

        JLabel scoreLabel = new JLabel(String.format("%d points", gameManager.getScore()));
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setForeground(Color.BLACK);
        scoreLabel.setBounds(6, 189, 200, 16);
        gameReportPanel.add(scoreLabel);

        JLabel lblNewLabel = new JLabel("Thank you for playing!");
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setBounds(244, 400, 333, 16);
        frame.getContentPane().add(lblNewLabel);

        JLabel partyReportLabel = new JLabel("Final Party:");
        partyReportLabel.setFont(new Font("Lucida Grande", Font.BOLD, 13));
        partyReportLabel.setBounds(410, 70, 200, 16);
        frame.getContentPane().add(partyReportLabel);

        // Panel displaying final party
        PartyPanel partyPanel = new PartyPanel(gameManager.getPlayer());
        partyPanel.setBounds(410, 86);
        partyPanel.applyToFrame(frame);

        // Button to exit the game.
        JButton exitButton = new JButton("Exit Game");
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        exitButton.setBounds(16, 400, 180, 30);
        frame.getContentPane().add(exitButton);
        exitButton.addActionListener(exitGameAction());

        // Button to start game over.
        JButton startOverButton = new JButton("Restart Game");
        startOverButton.setHorizontalAlignment(SwingConstants.CENTER);
        startOverButton.setBounds(607, 400, 180, 30);
        frame.getContentPane().add(startOverButton);
        startOverButton.addActionListener(restartGameAction());

        frame.setVisible(true);
    }

    /**
     * The action performed when using the exit game button (exits the game.)
     *
     * @return an action listener that is passed to the exit button.
     */
    private ActionListener exitGameAction() {
        return e -> gui.quit();
    }

    /**
     * The action performed when using the restart game button (starts over game from the start)
     *
     * @return an action listener that is passed to the startOver button.
     */
    private ActionListener restartGameAction() {
        return e -> {
            gui.quit();
            final GUI gui2 = new GUI();
        };
    }

    /**
     * Compute the coloring based on the percentage of current value to max
     *
     * @return Green if 50% or above, Orange if 49% to 25%, otherwise Red
     */
    private Color color(int value, int max) {
        final int percentage = value * 100 / max;
        if (percentage < 25)
            return new Color(112, 0, 0);
        if (percentage < 50)
            return new Color(196, 120, 0);
        return new Color(46, 139, 87);
    }

    /**
     * Compute the difficult name based on its scale
     *
     * @return Either normal, hard, or impossible
     */
    private String difficultyName() {
        if (gameManager.getDifficulty() == 1)
            return "Normal";
        if (gameManager.getDifficulty() == 2)
            return "Hard";
        return "Impossible";
    }
}
