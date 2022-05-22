package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;
import seng.monsters.ui.gui.Screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private JPanel monsterPanel;

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

    /**
     * Creates a JPanel that display a monster in the party
     * @param monster The monster to be displayed
     */
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
        monsterPanel.setVisible(isVisible);
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
        monsterPanel.setBounds(x, y, WIDTH, HEIGHT);
        monsterPanel.setLayout(null);
    }

    /**
     * Add this panel to the window frame
     *
     * @param frame The JFrame to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToFrame(JFrame frame) {
        frame.getContentPane().add(monsterPanel);
    }


    /**
     * Add this panel to another panel
     *
     * @param parentPanel The panel to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToPanel(JPanel parentPanel) {
        parentPanel.add(monsterPanel);
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
        Screen.imageIconFromResource(String.format("/images/small/%s.gif", monster.monsterType().toLowerCase()))
            .ifPresent(iconLabel::setIcon);

        nameButton.setText(String.format("%s", monster.getName()));

        levelLabel.setText(String.format("Lv. %d", monster.getLevel()));

        hpLabel.setText(String.format("%d/%d", monster.getCurrentHp(), monster.maxHp()));

        currHpPanel.setBounds(55, 34, 260 * monster.getCurrentHp() / monster.maxHp(), 10);
    }

    /**
     * Set the action listener for when the monster is selected
     * @param action The action listener to be called
     */
    public void addActionListener(ActionListener action) {
        nameButton.addActionListener(action);
    }

    /**
     * Initialize the UI element for this panel
     */
    private void render() {
        // Panel for the party slot
        monsterPanel = new JPanel();
        monsterPanel.setBackground(new Color(255, 250, 250));
        monsterPanel.setLayout(null);

        // Label for the picture of the monster
        iconLabel = new JLabel("");
        Screen.imageIconFromResource(String.format("/images/small/%s.gif", monster.monsterType().toLowerCase()))
            .ifPresent(iconLabel::setIcon);
        iconLabel.setBounds(6, 10, 37, 39);
        monsterPanel.add(iconLabel);

        // Button to select this specific monster, if required
        nameButton = new JButton();
        nameButton.setText(String.format("%s", monster.getName()));
        nameButton.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        nameButton.setBounds(54, 10, 194, 16);
        monsterPanel.add(nameButton);

        levelLabel = new JLabel();
        levelLabel.setText(String.format("Lv. %d", monster.getLevel()));
        levelLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        levelLabel.setBounds(254, 10, 61, 16);
        monsterPanel.add(levelLabel);

        hpLabel = new JLabel();
        hpLabel.setText(String.format("%d/%d", monster.getCurrentHp(), monster.maxHp()));
        hpLabel.setHorizontalAlignment(SwingConstants.LEADING);
        hpLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
        hpLabel.setBounds(55, 34, 220, 11);
        monsterPanel.add(hpLabel);

        // Panel displaying the current hp bar
        currHpPanel = new JPanel();
        currHpPanel.setBackground(Color.GREEN);
        currHpPanel.setBounds(55, 34, 260 * monster.getCurrentHp() / monster.maxHp(), 10);
        monsterPanel.add(currHpPanel);

        // Panel display the maximum hp bar
        JPanel maxHpPanel = new JPanel();
        maxHpPanel.setBackground(Color.RED);
        maxHpPanel.setBounds(55, 34, 260, 10);
        monsterPanel.add(maxHpPanel);

    }

    /**
     * The width of the panel
     */
    public static final int WIDTH = 327;

    /**
     * The height of the panel
     */
    public static final int HEIGHT = 55;
}
