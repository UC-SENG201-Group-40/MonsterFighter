//
//  BattleScreen.java
//  seng-monsters
//
//  Created by Vincent on 10:09 AM.
//
package seng.monsters.ui.gui;

import seng.monsters.model.BattleManager;
import seng.monsters.model.Environment;
import seng.monsters.model.Trainer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Objects;

/**
 * TODO: This is still a testing GUI, it works but need checks and testing to use it in the final application
 */
public final class BattleScreen implements BattleManager.UI {

    private final JFrame frmBattle;

    private final BattleManager battleManager;
    private Timer timer;

    private JLabel mon1Label0;
    private JLabel mon1Label1;
    private JLabel mon2Label0;
    private JLabel mon2Label1;
    private JLabel punchImg;
    private JLabel mon1Image;
    private JLabel mon2Image;
    private JButton quitBattle;
    private JLabel feed0;
    private JLabel feed1;
    private JLabel feed2;
    private JLabel feed3;
    private JRadioButton[] party1;
    private JRadioButton[] party2;

    /**
     * Create the application.
     */
    public BattleScreen(BattleManager manager) {
        frmBattle = new JFrame();
        battleManager = manager;
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frmBattle.setTitle("Battle");
        frmBattle.setBounds(100, 100, 819, 487);
        frmBattle.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmBattle.getContentPane().setLayout(null);

        frmBattle.setContentPane(new JLabel(new ImageIcon(Objects.requireNonNull(BattleScreen.class.getResource("/images/beach.jpeg")))));

        mon1Image = new JLabel();
        mon1Image.setHorizontalAlignment(SwingConstants.CENTER);
        URL mon1Url = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getMon1().getClass().getSimpleName().toLowerCase() + ".gif"
        ));
        ImageIcon mon1Icon = new ImageIcon(mon1Url);
        mon1Image.setIcon(mon1Icon);
        mon1Image.setBounds(62, 94, 200, 150);
        frmBattle.getContentPane().add(mon1Image);

        mon2Image = new JLabel();
        mon2Image.setHorizontalAlignment(SwingConstants.CENTER);
        URL mon2Url = Objects.requireNonNull(BattleScreen.class.getResource(
            "/images/" + battleManager.getMon2().getClass().getSimpleName().toLowerCase() + ".gif"
        ));
        ImageIcon mon2Icon = new ImageIcon(mon2Url);
        mon2Image.setIcon(mon2Icon);
        mon2Image.setBounds(551, 94, 200, 150);
        frmBattle.getContentPane().add(mon2Image);

        mon1Label0 = new JLabel(String.format(
            "%s (lvl: %d)", battleManager.getMon1().getName(), battleManager.getMon1().getLevel()
        ));
        mon1Label0.setBackground(new Color(192, 192, 192));
        mon1Label0.setHorizontalAlignment(SwingConstants.CENTER);
        mon1Label0.setBounds(62, 256, 200, 33);
        mon1Label0.setOpaque(true);
        frmBattle.getContentPane().add(mon1Label0);

        mon1Label1 = new JLabel(String.format(
            "HP: %d/%d", battleManager.getMon1().getCurrentHp(), battleManager.getMon1().maxHp()
        ));
        mon1Label1.setBackground(new Color(192, 192, 192));
        mon1Label1.setHorizontalAlignment(SwingConstants.CENTER);
        mon1Label1.setBounds(62, 289, 200, 33);
        mon1Label1.setOpaque(true);
        frmBattle.getContentPane().add(mon1Label1);

        mon2Label0 = new JLabel(String.format(
            "%s (lvl: %d)", battleManager.getMon2().getName(), battleManager.getMon2().getLevel()
        ));
        mon2Label0.setOpaque(true);
        mon2Label0.setHorizontalAlignment(SwingConstants.CENTER);
        mon2Label0.setBackground(Color.LIGHT_GRAY);
        mon2Label0.setBounds(551, 256, 200, 33);
        frmBattle.getContentPane().add(mon2Label0);

        mon2Label1 = new JLabel(String.format(
            "HP: %d/%d", battleManager.getMon2().getCurrentHp(), battleManager.getMon2().maxHp()
        ));
        mon2Label1.setBackground(new Color(192, 192, 192));
        mon2Label1.setHorizontalAlignment(SwingConstants.CENTER);
        mon2Label1.setBounds(551, 289, 200, 33);
        mon2Label1.setOpaque(true);
        frmBattle.getContentPane().add(mon2Label1);

        JButton startBattle = new JButton("Start");
        startBattle.setBounds(678, 400, 117, 29);
        frmBattle.getContentPane().add(startBattle);

        quitBattle = new JButton("End");
        quitBattle.setBounds(40, 400, 117, 29);
        quitBattle.setEnabled(false);
        quitBattle.setVisible(false);
        frmBattle.getContentPane().add(quitBattle);

        punchImg = new JLabel();
        punchImg.setEnabled(true);
        punchImg.setVisible(false);
        punchImg.setHorizontalAlignment(SwingConstants.CENTER);
        punchImg.setBounds(350, 237, 108, 86);
        frmBattle.getContentPane().add(punchImg);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setBounds(193, 330, 457, 118);
        frmBattle.getContentPane().add(panel);
        panel.setLayout(null);

        feed0 = new JLabel("");
        feed0.setForeground(Color.GREEN);
        feed0.setBounds(6, 5, 445, 16);
        panel.add(feed0);

        feed1 = new JLabel("");
        feed1.setForeground(Color.GREEN);
        feed1.setBounds(6, 33, 445, 16);
        panel.add(feed1);

        feed2 = new JLabel("");
        feed2.setForeground(Color.GREEN);
        feed2.setBounds(6, 61, 445, 16);
        panel.add(feed2);

        feed3 = new JLabel("");
        feed3.setForeground(Color.GREEN);
        feed3.setBounds(6, 89, 445, 16);
        panel.add(feed3);

        JPanel party1 = new JPanel();
        party1.setBounds(62, 26, 148, 33);
        frmBattle.getContentPane().add(party1);

        JRadioButton party1Check0 = new JRadioButton();
        party1.add(party1Check0);

        JRadioButton party1Check1 = new JRadioButton();
        party1.add(party1Check1);

        JRadioButton party1Check2 = new JRadioButton();
        party1.add(party1Check2);

        JRadioButton party1Check3 = new JRadioButton();
        party1.add(party1Check3);

        this.party1 = new JRadioButton[]{party1Check0, party1Check1, party1Check2, party1Check3};

        JPanel party2 = new JPanel();
        party2.setBounds(603, 26, 148, 33);
        frmBattle.getContentPane().add(party2);

        JRadioButton party2Check3 = new JRadioButton();
        party2.add(party2Check3);

        JRadioButton party2Check2 = new JRadioButton();
        party2.add(party2Check2);

        JRadioButton party2Check1 = new JRadioButton();
        party2.add(party2Check1);

        JRadioButton party2Check0 = new JRadioButton();
        party2.add(party2Check0);

        this.party2 = new JRadioButton[]{party2Check0, party2Check1, party2Check2, party2Check3};

        repaintParties();

        startBattle.addActionListener(e -> {
            if (battleManager.isSettled())
                return;

            final var t = new Timer(25, e1 ->
                battleManager.nextIteration()
            );
            t.start();
            onStart();

            timer = t;
        });
        quitBattle.addActionListener(onQuit());

        frmBattle.setVisible(true);

    }

    private void repaint() {
        mon1Label0.setText(String.format(
            "%s (lvl: %d)", battleManager.getMon1().getName(), battleManager.getMon1().getLevel()
        ));
        mon1Label1.setText(String.format(
            "HP: %d/%d", battleManager.getMon1().getCurrentHp(), battleManager.getMon1().maxHp()
        ));
        mon2Label0.setText(String.format(
            "%s (lvl: %d)", battleManager.getMon2().getName(), battleManager.getMon2().getLevel()
        ));
        mon2Label1.setText(String.format(
            "HP: %d/%d", battleManager.getMon2().getCurrentHp(), battleManager.getMon2().maxHp()
        ));

        repaintFeeds();
    }

    private void repaintParties() {
        final var playerParty = battleManager.getPlayer1().getParty();
        final var enemyParty = battleManager.getPlayer2().getParty();
        for (var i = 0; i < 4; i++) {
            this.party1[i].setSelected(i < playerParty.size() && !playerParty.get(i).isFainted());
            this.party2[i].setSelected(i < enemyParty.size() && !enemyParty.get(i).isFainted());
        }
    }

    private void repaintFeeds() {
        final var feeds = battleManager.getFeeds();
        final var feedLabels = new JLabel[]{feed0, feed1, feed2, feed3};
        final var latestFeeds = feeds.stream().toList().subList(
            Math.max(feeds.size() - 4, 0),
            feeds.size()
        );
        for (var i = 0; i < latestFeeds.size(); i++) {
            feedLabels[i].setText(latestFeeds.get(i));
        }
    }


    private ActionListener onQuit() {
        return e -> frmBattle.dispose();
    }

    public void onStart() {
        final var isMon1Turn = battleManager.getMon1().speed() >= battleManager.getMon2().speed();
        punchImg.setIcon(
            new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                "/images/" + (isMon1Turn ? "punch-true" : "punch-false") + ".png"
            )))
        );
        repaintParties();
    }


    @Override
    public void onEachFrame(boolean isMon1Turn, int pos) {
        punchImg.setVisible(true);
        punchImg.setBounds(pos, 134, 108, 86);
    }

    @Override
    public void onEachDamage(boolean isMon1Turn, int dmg) {
        repaint();
        punchImg.setVisible(false);
        punchImg.setIcon(
            new ImageIcon(Objects.requireNonNull(this.getClass().getResource(
                "/images/" + (!isMon1Turn ? "punch-true" : "punch-false") + ".png"
            )))
        );
    }

    @Override
    public void onEachNextMonster(boolean isMon1Turn) {
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
        quitBattle.setVisible(true);
        quitBattle.setEnabled(true);
        repaintFeeds();
        repaintParties();
        timer.stop();
    }

    public static void make(BattleManager battleManager) {
        EventQueue.invokeLater(() -> {
            final var screen = new BattleScreen(battleManager);
        });
    }
}
