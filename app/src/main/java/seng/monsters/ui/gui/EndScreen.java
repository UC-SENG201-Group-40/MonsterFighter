package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Monster;
import seng.monsters.ui.gui.components.PartySlotPanel;
import seng.monsters.ui.gui.components.PopUp;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The final screen displaying the player's final stats.
 */
public class EndScreen extends Screen {

    private final List<Monster> party;

    /**
     * Creates the EndScreen
     */
    public EndScreen(GUI gui, GameManager gameManager) {
        super(gui, gameManager);
        party = gameManager.getTrainer().getParty();
    }

    @Override
    public void render() {

        // Label displaying the player's name.
        JLabel playerNameLabel = new JLabel(String.format("%s...", gameManager.getTrainer().getName()));
        playerNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerNameLabel.setBounds(336, 32, 150, 20);
        playerNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 14));
        frame.getContentPane().add(playerNameLabel);

        // Label displaying the end result of the game (victory or failure).
        final var maxDays = gameManager.getMaxDays();

        final var endResultString = (gameManager.hasEnded()) ?
                String.format("YOU SURVIVED THE %d DAY ONSLAUGHT.", maxDays) :
                String.format("YOU FAILED THE %d DAY ONSLAUGHT.", maxDays);

        JLabel endResultLabel = new JLabel(endResultString);
        endResultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        endResultLabel.setBounds(0, 68, 807, 32);
        endResultLabel.setFont(new Font("Lucida Grande", Font.BOLD, 39));
        frame.getContentPane().add(endResultLabel);

        // Label displaying the last day survived.
        JLabel finalDayLabel = new JLabel(String.format("Final day: %d", gameManager.getCurrentDay()-1));
        finalDayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        finalDayLabel.setBounds(362, 106, 96, 16);
        frame.getContentPane().add(finalDayLabel);

        // Label displaying the player's final gold.
        JLabel finalGoldLabel = new JLabel(String.format("Final gold: %d", gameManager.getGold()));
        finalDayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        finalGoldLabel.setBounds(180, 160, 180, 30);
        finalGoldLabel.setFont(new Font("Lucia Grande", Font.BOLD, 24));
        frame.getContentPane().add(finalGoldLabel);

        // Label displaying the player's final score.
        JLabel finalScoreLabel = new JLabel(String.format("Final score: %d", gameManager.getScore()));
        finalDayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        finalScoreLabel.setBounds(479, 160, 180, 30);
        finalScoreLabel.setFont(new Font("Lucia Grande", Font.BOLD, 24));
        frame.getContentPane().add(finalScoreLabel);

        // Label displaying preceding the player's final party panel
        JLabel finalPartyLabel = new JLabel("Final party:");
        finalDayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        finalPartyLabel.setBounds(345, 210, 130, 30);
        finalPartyLabel.setFont(new Font("Lucia Grande", Font.BOLD, 24));
        frame.getContentPane().add(finalPartyLabel);

        // Panel containing each monster of the player's party
        JPanel partyPanel = new JPanel();
        partyPanel.setOpaque(false);
        partyPanel.setLayout(null);
        partyPanel.setBounds((PopUp.WIDTH/2) - PartySlotPanel.WIDTH - 8, 250,
                2 * PartySlotPanel.WIDTH + 8, 2 * PartySlotPanel.HEIGHT + 8);
        for (var i = 0; i < party.size(); i++) {
            PartySlotPanel monsterSlot = new PartySlotPanel(party.get(i));
            monsterSlot.setBounds((i % 2) * (PartySlotPanel.WIDTH + 8),
                    (int)(i * 0.5) * (PartySlotPanel.HEIGHT + 8));
            monsterSlot.applyToPanel(partyPanel);
        }
        frame.getContentPane().add(partyPanel);

        // Label thanking the player
        JLabel thankYouLabel = new JLabel("Thanks for playing!");
        finalDayLabel.setHorizontalAlignment(SwingConstants.CENTER);
        thankYouLabel.setBounds(339, 390, 150, 30);
        thankYouLabel.setFont(new Font("Lucia Grande", Font.PLAIN, 16));
        frame.getContentPane().add(thankYouLabel);

        frame.setVisible(true);
    }
}
