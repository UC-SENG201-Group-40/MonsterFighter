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
     * The label for monster 1 name and level
     */
    private JLabel mon1NameLabel;

    /**
     * The label for monster 1 hp
     */
    private JLabel mon1HpLabel;

    /**
     * The label for monster 2 name and level
     */
    private JLabel mon2NameLabel;

    /**
     * The label for monster 2 hp
     */
    private JLabel mon2HpLabel;

    /**
     * The image for the punching moving icon
     */
    private JLabel punchImg;

    /**
     * The monster 1 image
     */
    private JLabel mon1Image;

    /**
     * The monster 2 image
     */
    private JLabel mon2Image;

    /**
     * The fire image for monster 1
     */
    private JLabel fire1Image;

    /**
     * The fire image for monster 2
     */
    private JLabel fire2Image;

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
     * The trainer 1's party radio button
     */
    private JRadioButton[] party1Display;

    /**
     * The trainer 2's party radio button
     */
    private JRadioButton[] party2Display;

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


        fire1Image = new JLabel();
        fire1Image.setHorizontalAlignment(SwingConstants.CENTER);
        fire1Image.setIcon(new ImageIcon(
            Objects.requireNonNull(BattleScreen.class.getResource(
                "/images/fire.gif"
            ))
        ));
        fire1Image.setBounds(62, 94, 200, 150);
        fire1Image.setVisible(false);
        frame.getContentPane().add(fire1Image);

        fire2Image = new JLabel();
        fire2Image.setHorizontalAlignment(SwingConstants.CENTER);
        fire2Image.setIcon(new ImageIcon(
            Objects.requireNonNull(BattleScreen.class.getResource(
                "/images/fire.gif"
            ))
        ));
        fire2Image.setBounds(551, 94, 200, 150);
        fire2Image.setVisible(false);
        frame.getContentPane().add(fire2Image);

        mon1Image = new JLabel();
        mon1Image.setHorizontalAlignment(SwingConstants.CENTER);
        URL mon1Url = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getMon1().monsterType().toLowerCase() + ".gif"
        ));
        ImageIcon mon1Icon = new ImageIcon(mon1Url);
        mon1Image.setIcon(mon1Icon);
        mon1Image.setBounds(62, 94, 200, 150);
        frame.getContentPane().add(mon1Image);

        mon2Image = new JLabel();
        mon2Image.setHorizontalAlignment(SwingConstants.CENTER);
        URL mon2Url = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getMon2().monsterType().toLowerCase() + ".gif"
        ));
        ImageIcon mon2Icon = new ImageIcon(mon2Url);
        mon2Image.setIcon(mon2Icon);
        mon2Image.setBounds(551, 94, 200, 150);
        frame.getContentPane().add(mon2Image);

        mon1NameLabel = new JLabel(String.format(
            "%s (lvl: %d)", battleManager.getMon1().getName(), battleManager.getMon1().getLevel()
        ));
        mon1NameLabel.setBackground(new Color(192, 192, 192));
        mon1NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mon1NameLabel.setBounds(62, 256, 200, 33);
        mon1NameLabel.setOpaque(true);
        frame.getContentPane().add(mon1NameLabel);

        mon1HpLabel = new JLabel(String.format(
            "HP: %d/%d", battleManager.getMon1().getCurrentHp(), battleManager.getMon1().maxHp()
        ));
        mon1HpLabel.setBackground(new Color(192, 192, 192));
        mon1HpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mon1HpLabel.setBounds(62, 289, 200, 33);
        mon1HpLabel.setOpaque(true);
        frame.getContentPane().add(mon1HpLabel);

        mon2NameLabel = new JLabel(String.format(
            "%s (lvl: %d)", battleManager.getMon2().getName(), battleManager.getMon2().getLevel()
        ));
        mon2NameLabel.setOpaque(true);
        mon2NameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mon2NameLabel.setBackground(Color.LIGHT_GRAY);
        mon2NameLabel.setBounds(551, 256, 200, 33);
        frame.getContentPane().add(mon2NameLabel);

        mon2HpLabel = new JLabel(String.format(
            "HP: %d/%d", battleManager.getMon2().getCurrentHp(), battleManager.getMon2().maxHp()
        ));
        mon2HpLabel.setBackground(new Color(192, 192, 192));
        mon2HpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mon2HpLabel.setBounds(551, 289, 200, 33);
        mon2HpLabel.setOpaque(true);
        frame.getContentPane().add(mon2HpLabel);

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

        this.party1Display = new JRadioButton[]{party1Check0, party1Check1, party1Check2, party1Check3};

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

        this.party2Display = new JRadioButton[]{party2Check0, party2Check1, party2Check2, party2Check3};

        repaintParties();

        startButton.addActionListener(onStart());
        quitButton.addActionListener(onQuit());

        frame.setVisible(true);

    }

    /**
     * Repaint / update all the labels
     */
    private void repaint() {
        mon1NameLabel.setText(String.format(
            "%s (lvl: %d)", battleManager.getMon1().getName(), battleManager.getMon1().getLevel()
        ));
        mon1HpLabel.setText(String.format(
            "HP: %d/%d", battleManager.getMon1().getCurrentHp(), battleManager.getMon1().maxHp()
        ));
        mon2NameLabel.setText(String.format(
            "%s (lvl: %d)", battleManager.getMon2().getName(), battleManager.getMon2().getLevel()
        ));
        mon2HpLabel.setText(String.format(
            "HP: %d/%d", battleManager.getMon2().getCurrentHp(), battleManager.getMon2().maxHp()
        ));

        repaintFeeds();
    }

    /**
     * Repaint / update the party radio buttons
     */
    private void repaintParties() {
        final var playerParty = battleManager.getPlayer1().getParty();
        final var enemyParty = battleManager.getPlayer2().getParty();
        for (var i = 0; i < 4; i++) {
            this.party1Display[i].setSelected(i < playerParty.size() && !playerParty.get(i).isFainted());
            this.party2Display[i].setSelected(i < enemyParty.size() && !enemyParty.get(i).isFainted());
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
        return e -> gui.navigateBackToMainMenu();
    }

    /**
     * The action performed when the battle is being started
     * @return An action listener for the start button
     */
    private ActionListener onStart() {
        return e -> {
            if (battleManager.isSettled())
                return;

            final var t = new Timer(25, e1 ->
                battleManager.nextIteration()
            );
            t.start();
            timer = t;

            final var isMon1Turn = battleManager.getMon1().speed() >= battleManager.getMon2().speed();
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
    public void onEachFrame(boolean isMon1Turn, int pos, int percentage) {
        punchImg.setVisible(true);
        punchImg.setBounds(pos, 134, 108, 86);
        if (percentage > 40) {
            fire1Image.setVisible(false);
            fire2Image.setVisible(false);
        }
    }

    @Override
    public void onEachDamage(boolean isMon1Turn, int dmg) {
        repaint();
        punchImg.setVisible(false);
        fire1Image.setVisible(!isMon1Turn);
        fire2Image.setVisible(isMon1Turn);
        punchImg.setIcon(
            new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                "/images/" + (!isMon1Turn ? "punch-true" : "punch-false") + ".png"
            )))
        );
    }

    @Override
    public void onEachNextMonster(boolean isMon1Turn) {
        fire1Image.setVisible(false);
        fire2Image.setVisible(false);
        punchImg.setIcon(
            new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                "/images/" + (isMon1Turn ? "punch-true" : "punch-false") + ".png"
            )))
        );
        URL mon1Url = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getMon1().getClass().getSimpleName().toLowerCase() + ".gif"
        ));
        ImageIcon mon1Icon = new ImageIcon(mon1Url);
        mon1Image.setIcon(mon1Icon);
        URL mon2Url = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getMon2().getClass().getSimpleName().toLowerCase() + ".gif"
        ));
        ImageIcon mon2Icon = new ImageIcon(mon2Url);
        mon2Image.setIcon(mon2Icon);

        repaintParties();

        repaint();
    }

    @Override
    public void onEnd(boolean isMon1Turn) {
        punchImg.setVisible(false);
        quitButton.setVisible(true);
        quitButton.setEnabled(true);
        repaintFeeds();
        repaintParties();
        timer.stop();
    }

    @Override
    public void dispose() {
        if (battleManager.hasPlayerWon()) {
            gameManager.setGold(gameManager.getGold() + battleManager.goldReward() * gameManager.getDifficulty());
            gameManager.setScore(gameManager.getScore() + battleManager.scoreReward() * gameManager.getDifficulty());
        }
        super.dispose();
    }
}
