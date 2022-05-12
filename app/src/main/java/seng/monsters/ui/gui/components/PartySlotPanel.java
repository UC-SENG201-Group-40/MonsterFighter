package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * A panel for the individual slot for the party
 */
public final class PartySlotPanel {
    /**
     * The monster to be displayed
     */
    private Monster monster;

    /**
     * The panel itself
     */
    private JPanel panel;

    /**
     * The label for the small icon
     */
    private JLabel iconLabel;

    /**
     * The label for the name
     */
    private JButton nameButton;

    /**
     * The level label
     */
    private JLabel levelLabel;

    /**
     * The label for the hp
     */
    private JLabel hpLabel;

    /**
     * The panel for the current hp bar
     */
    private JPanel currHpPanel;

    public PartySlotPanel(Monster monster) {
        this.monster = monster;
        render();
    }


    /**
     * Set if the panel should be visible or not
     *
     * @param isVisible A boolean if the panel should be visible
     */
    public void setVisible(boolean isVisible) {
        panel.setVisible(isVisible);
        if (isVisible)
            refresh();
    }


    /**
     * Set the boundary / location of the panel on the screen
     *
     * @param x The horizontal location
     * @param y The vertical location
     */
    public void setBounds(int x, int y) {
        panel.setBounds(x, y, WIDTH, HEIGHT);
        panel.setLayout(null);
    }

    /**
     * Add this panel to the window frame
     *
     * @param frame The JFrame to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToFrame(JFrame frame) {
        frame.getContentPane().add(panel);
    }


    /**
     * Add this panel to another panel
     *
     * @param panel The panel to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToPanel(JPanel parentPanel) {
        parentPanel.add(panel);
    }

    /**
     * Refresh the panel with a new monster
     *
     * @param monster The replacement monster to be displayed
     */
    public void refresh(Monster monster) {
        this.monster = monster;
        refresh();
    }

    /**
     * Refresh the panel with the current monster.
     * <p>
     * Used to update the display to match the change in the Monster
     */
    public void refresh() {
        iconLabel.setIcon(new ImageIcon(
            Objects.requireNonNull(
                PartySlotPanel.class.getResource(
                    String.format("/images/small/%s.gif", monster.monsterType().toLowerCase())
                )
            )
        ));

        nameButton.setText(String.format("%s (%s)", monster.getName(), monster.monsterType()));

        levelLabel.setText(String.format("Lv. %d", monster.getLevel()));

        hpLabel.setText(String.format("%d/%d", monster.getCurrentHp(), monster.maxHp()));

        currHpPanel.setBounds(55, 34, 220 * monster.getCurrentHp() / monster.maxHp(), 10);
    }

    public void addActionListener(ActionListener action) {
        nameButton.addActionListener(action);
    }

    /**
     * Initialize the UI element for this panel
     */
    private void render() {
        panel = new JPanel();
        panel.setBackground(new Color(255, 250, 250));
        panel.setLayout(null);

        iconLabel = new JLabel("");
        iconLabel.setIcon(new ImageIcon(
            Objects.requireNonNull(
                this.getClass().getResource(
                    String.format("/images/small/%s.gif", monster.monsterType().toLowerCase())
                )
            )
        ));
        iconLabel.setBounds(6, 10, 37, 39);
        panel.add(iconLabel);

        nameButton = new JButton();
        nameButton.setText(String.format("%s (%s)", monster.getName(), monster.monsterType()));
        nameButton.setBounds(54, 10, 154, 16);
        panel.add(nameButton);

        levelLabel = new JLabel();
        levelLabel.setText(String.format("Lv. %d", monster.getLevel()));
        levelLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        levelLabel.setBounds(214, 10, 61, 16);
        panel.add(levelLabel);

        hpLabel = new JLabel();
        hpLabel.setText(String.format("%d/%d", monster.getCurrentHp(), monster.maxHp()));
        hpLabel.setHorizontalAlignment(SwingConstants.LEADING);
        hpLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        hpLabel.setBounds(55, 34, 220, 11);
        panel.add(hpLabel);

        currHpPanel = new JPanel();
        currHpPanel.setBackground(Color.GREEN);
        currHpPanel.setBounds(55, 34, 220 * monster.getCurrentHp() / monster.maxHp(), 10);
        panel.add(currHpPanel);

        JPanel maxHpPanel = new JPanel();
        maxHpPanel.setBackground(Color.RED);
        maxHpPanel.setBounds(55, 34, 220, 10);
        panel.add(maxHpPanel);

    }

    /**
     * The width of the panel
     */
    public static final int WIDTH = 287;

    /**
     * The height of the panel
     */
    public static final int HEIGHT = 55;
}
