//
//  BattleScreen.java
//  seng-monsters
//
//  Created by Vincent on 10:09 AM.
//
package seng.monsters.ui.gui;

import seng.monsters.model.BattleManager;
import seng.monsters.model.GameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Objects;

/**
 * A screen to display the battle and all the fancy actions
 */
public final class BattleScreen extends Screen implements BattleManager.UI {

    /**
     * The battle manager to manage the logic in battling
     */
    private final BattleManager battleManager;

    /**
     * The timer used to create a loop without breaking the GUI
     */
    private Timer timer;

    /**
     * The label for player's monster name and level
     */
    private JLabel playerMonsterNameLabel;

    /**
     * The label for player's monster hp
     */
    private JLabel playerMonsterHpLabel;

    /**
     * The label for enemy's monster name and level
     */
    private JLabel enemyMonsterNameLabel;

    /**
     * The label for enemy's monster hp
     */
    private JLabel enemyMonsterHpLabel;

    /**
     * The image for the punching moving icon
     */
    private JLabel punchImg;

    /**
     * The player's monster image
     */
    private JLabel playerMonsterImage;

    /**
     * The enemy's monster image
     */
    private JLabel enemyPlayerImage;

    /**
     * The fire image for player's monster
     */
    private JLabel playerFireImage;

    /**
     * The fire image for enemy's monster
     */
    private JLabel enemyFireImage;

    /**
     * The start battle button
     */
    private JButton startButton;

    /**
     * The quit battle button
     */
    private JButton quitButton;

    /**
     * The feed labels
     */
    private JLabel[] feedLabels;

    /**
     * The player's party radio button
     */
    private JRadioButton[] playerPartyButtons;

    /**
     * The enemy's party radio button
     */
    private JRadioButton[] enemyPartyButtons;

    /**
     * Create the application.
     */
    public BattleScreen(GUI gui, GameManager gameManager, int index) {
        super(gui, gameManager);
        battleManager = gameManager.prepareBattle(this, index);
    }

    @Override
    public void render() {
        frame.setContentPane(new JLabel(
            new ImageIcon(
                Objects.requireNonNull(BattleScreen.class.getResource(
                    String.format("/images/%s.jpeg", gameManager.getEnvironment().toString())
                )))
        ));


        playerFireImage = new JLabel();
        playerFireImage.setHorizontalAlignment(SwingConstants.CENTER);
        playerFireImage.setIcon(new ImageIcon(
            Objects.requireNonNull(BattleScreen.class.getResource(
                "/images/fire.gif"
            ))
        ));
        playerFireImage.setBounds(62, 94, 200, 150);
        playerFireImage.setVisible(false);
        frame.getContentPane().add(playerFireImage);

        enemyFireImage = new JLabel();
        enemyFireImage.setHorizontalAlignment(SwingConstants.CENTER);
        enemyFireImage.setIcon(new ImageIcon(
            Objects.requireNonNull(BattleScreen.class.getResource(
                "/images/fire.gif"
            ))
        ));
        enemyFireImage.setBounds(551, 94, 200, 150);
        enemyFireImage.setVisible(false);
        frame.getContentPane().add(enemyFireImage);

        playerMonsterImage = new JLabel();
        playerMonsterImage.setHorizontalAlignment(SwingConstants.CENTER);
        URL playerMonUrl = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getBattlingPlayerMonster().monsterType().toLowerCase() + ".gif"
        ));
        ImageIcon playerMonIcon = new ImageIcon(playerMonUrl);
        playerMonsterImage.setIcon(playerMonIcon);
        playerMonsterImage.setBounds(62, 94, 200, 150);
        frame.getContentPane().add(playerMonsterImage);

        enemyPlayerImage = new JLabel();
        enemyPlayerImage.setHorizontalAlignment(SwingConstants.CENTER);
        URL enemyMonUrl = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getBattlingEnemyMonster().monsterType().toLowerCase() + ".gif"
        ));
        ImageIcon enemyMonIcon = new ImageIcon(enemyMonUrl);
        enemyPlayerImage.setIcon(enemyMonIcon);
        enemyPlayerImage.setBounds(551, 94, 200, 150);
        frame.getContentPane().add(enemyPlayerImage);

        playerMonsterNameLabel = new JLabel(String.format(
            "%s (lvl: %d)", battleManager.getBattlingPlayerMonster().getName(), battleManager.getBattlingPlayerMonster().getLevel()
        ));
        playerMonsterNameLabel.setBackground(new Color(192, 192, 192));
        playerMonsterNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerMonsterNameLabel.setBounds(62, 256, 200, 33);
        playerMonsterNameLabel.setOpaque(true);
        frame.getContentPane().add(playerMonsterNameLabel);

        playerMonsterHpLabel = new JLabel(String.format(
            "HP: %d/%d", battleManager.getBattlingPlayerMonster().getCurrentHp(), battleManager.getBattlingPlayerMonster().maxHp()
        ));
        playerMonsterHpLabel.setBackground(new Color(192, 192, 192));
        playerMonsterHpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        playerMonsterHpLabel.setBounds(62, 289, 200, 33);
        playerMonsterHpLabel.setOpaque(true);
        frame.getContentPane().add(playerMonsterHpLabel);

        enemyMonsterNameLabel = new JLabel(String.format(
            "%s (lvl: %d)", battleManager.getBattlingEnemyMonster().getName(), battleManager.getBattlingEnemyMonster().getLevel()
        ));
        enemyMonsterNameLabel.setOpaque(true);
        enemyMonsterNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enemyMonsterNameLabel.setBackground(Color.LIGHT_GRAY);
        enemyMonsterNameLabel.setBounds(551, 256, 200, 33);
        frame.getContentPane().add(enemyMonsterNameLabel);

        enemyMonsterHpLabel = new JLabel(String.format(
            "HP: %d/%d", battleManager.getBattlingEnemyMonster().getCurrentHp(), battleManager.getBattlingEnemyMonster().maxHp()
        ));
        enemyMonsterHpLabel.setBackground(new Color(192, 192, 192));
        enemyMonsterHpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        enemyMonsterHpLabel.setBounds(551, 289, 200, 33);
        enemyMonsterHpLabel.setOpaque(true);
        frame.getContentPane().add(enemyMonsterHpLabel);

        startButton = new JButton("Start");
        startButton.setBounds(678, 400, 117, 29);
        frame.getContentPane().add(startButton);

        quitButton = new JButton("End");
        quitButton.setBounds(40, 400, 117, 29);
        quitButton.setEnabled(false);
        quitButton.setVisible(false);
        frame.getContentPane().add(quitButton);

        punchImg = new JLabel();
        punchImg.setEnabled(true);
        punchImg.setVisible(false);
        punchImg.setHorizontalAlignment(SwingConstants.CENTER);
        punchImg.setBounds(350, 237, 108, 86);
        frame.getContentPane().add(punchImg);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setBounds(193, 330, 457, 118);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel feed0 = new JLabel("");
        feed0.setForeground(Color.GREEN);
        feed0.setBounds(6, 5, 445, 16);
        panel.add(feed0);

        JLabel feed1 = new JLabel("");
        feed1.setForeground(Color.GREEN);
        feed1.setBounds(6, 33, 445, 16);
        panel.add(feed1);

        JLabel feed2 = new JLabel("");
        feed2.setForeground(Color.GREEN);
        feed2.setBounds(6, 61, 445, 16);
        panel.add(feed2);

        JLabel feed3 = new JLabel("");
        feed3.setForeground(Color.GREEN);
        feed3.setBounds(6, 89, 445, 16);
        panel.add(feed3);

        this.feedLabels = new JLabel[]{feed0, feed1, feed2, feed3};

        JPanel party1 = new JPanel();
        party1.setBounds(62, 26, 148, 33);
        frame.getContentPane().add(party1);

        JRadioButton party1Check0 = new JRadioButton();
        party1.add(party1Check0);

        JRadioButton party1Check1 = new JRadioButton();
        party1.add(party1Check1);

        JRadioButton party1Check2 = new JRadioButton();
        party1.add(party1Check2);

        JRadioButton party1Check3 = new JRadioButton();
        party1.add(party1Check3);

        this.playerPartyButtons = new JRadioButton[]{party1Check0, party1Check1, party1Check2, party1Check3};

        JPanel party2 = new JPanel();
        party2.setBounds(603, 26, 148, 33);
        frame.getContentPane().add(party2);

        JRadioButton party2Check3 = new JRadioButton();
        party2.add(party2Check3);

        JRadioButton party2Check2 = new JRadioButton();
        party2.add(party2Check2);

        JRadioButton party2Check1 = new JRadioButton();
        party2.add(party2Check1);

        JRadioButton party2Check0 = new JRadioButton();
        party2.add(party2Check0);

        this.enemyPartyButtons = new JRadioButton[]{party2Check0, party2Check1, party2Check2, party2Check3};

        repaintParties();

        startButton.addActionListener(onStart());
        quitButton.addActionListener(onQuit());

        frame.setVisible(true);

    }

    /**
     * Repaint / update all the labels
     */
    private void repaint() {
        playerMonsterNameLabel.setText(String.format(
            "%s (lvl: %d)", battleManager.getBattlingPlayerMonster().getName(), battleManager.getBattlingPlayerMonster().getLevel()
        ));
        playerMonsterHpLabel.setText(String.format(
            "HP: %d/%d", battleManager.getBattlingPlayerMonster().getCurrentHp(), battleManager.getBattlingPlayerMonster().maxHp()
        ));
        enemyMonsterNameLabel.setText(String.format(
            "%s (lvl: %d)", battleManager.getBattlingEnemyMonster().getName(), battleManager.getBattlingEnemyMonster().getLevel()
        ));
        enemyMonsterHpLabel.setText(String.format(
            "HP: %d/%d", battleManager.getBattlingEnemyMonster().getCurrentHp(), battleManager.getBattlingEnemyMonster().maxHp()
        ));

        repaintFeeds();
    }

    /**
     * Repaint / update the party radio buttons
     */
    private void repaintParties() {
        final var playerParty = battleManager.getPlayer().getParty();
        final var enemyParty = battleManager.getEnemy().getParty();
        for (var i = 0; i < 4; i++) {
            this.playerPartyButtons[i].setSelected(i < playerParty.size() && !playerParty.get(i).isFainted());
            this.enemyPartyButtons[i].setSelected(i < enemyParty.size() && !enemyParty.get(i).isFainted());
        }
    }

    /**
     * Repaint / update the display feed
     */
    private void repaintFeeds() {
        final var feeds = battleManager.getFeeds();
        final var latestFeeds = feeds.stream().toList().subList(
            Math.max(feeds.size() - 4, 0),
            feeds.size()
        );
        for (var i = 0; i < latestFeeds.size(); i++) {
            feedLabels[i].setText(latestFeeds.get(i));
        }
    }


    /**
     * The action performed when quiting the battle screen
     * @return An action listener for the quit button
     */
    private ActionListener onQuit() {
        return ignoredEvent -> gui.navigateBackToMainMenu();
    }

    /**
     * The action performed when the battle is being started
     * @return An action listener for the start button
     */
    private ActionListener onStart() {
        return ignored -> {
            if (battleManager.isSettled())
                return;

            // Setup timer to create loop
            timer = new Timer(25, event -> battleManager.nextIteration());
            timer.start();

            // Update the sprites to be position properly in the direction of the defending monster
            final var isMon1Turn = battleManager.getBattlingPlayerMonster().speed() >= battleManager.getBattlingEnemyMonster().speed();
            punchImg.setIcon(
                new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                    "/images/" + (isMon1Turn ? "punch-true" : "punch-false") + ".png"
                )))
            );
            repaintParties();
            startButton.setEnabled(false);
        };
    }

    @Override
    public void onEachFrame(int percentage) {
        punchImg.setVisible(true);
        punchImg.setBounds(62 + BattleManager.PSEUDO_MAX_POSITION * percentage / 5, 134, 108, 86);
        if (percentage > 40 && percentage < 60) {
            playerFireImage.setVisible(false);
            enemyFireImage.setVisible(false);
        }
    }

    @Override
    public void onEachDamage(boolean isPlayerTurn, int dmg) {
        repaint();
        punchImg.setVisible(false);
        playerFireImage.setVisible(!isPlayerTurn);
        enemyFireImage.setVisible(isPlayerTurn);
        punchImg.setIcon(
            new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                "/images/" + (!isPlayerTurn ? "punch-true" : "punch-false") + ".png"
            )))
        );
    }

    @Override
    public void onEachNextMonster(boolean isPlayerTurn) {
        playerFireImage.setVisible(false);
        enemyFireImage.setVisible(false);
        punchImg.setIcon(
            new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                "/images/" + (isPlayerTurn ? "punch-true" : "punch-false") + ".png"
            )))
        );
        URL playerMonUrl = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getBattlingPlayerMonster().getClass().getSimpleName().toLowerCase() + ".gif"
        ));
        ImageIcon playerMonIcon = new ImageIcon(playerMonUrl);
        playerMonsterImage.setIcon(playerMonIcon);
        URL enemyMonUrl = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getBattlingEnemyMonster().getClass().getSimpleName().toLowerCase() + ".gif"
        ));
        ImageIcon enemyMonIcon = new ImageIcon(enemyMonUrl);
        enemyPlayerImage.setIcon(enemyMonIcon);

        repaintParties();

        repaint();
    }

    @Override
    public void onEnd() {
        punchImg.setVisible(false);
        quitButton.setVisible(true);
        quitButton.setEnabled(true);
        repaintFeeds();
        repaintParties();
        timer.stop();
        if (battleManager.hasPlayerWon()) {
            gameManager.setGold(gameManager.getGold() + battleManager.goldReward() * gameManager.getDifficulty());
            gameManager.setScore(gameManager.getScore() + battleManager.scoreReward() * gameManager.getDifficulty());
        }
    }
}
