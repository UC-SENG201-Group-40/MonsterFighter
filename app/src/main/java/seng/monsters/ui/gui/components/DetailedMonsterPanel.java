package seng.monsters.ui.gui.components;

import seng.monsters.model.Monster;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * A fully detailed monster panel with the size of <code>300 x 251</code>
 * <p>
 * Include: <ul>
 * <li> Name (Monster typename) </li>
 * <li> Level </li>
 * <li> Current Hp / Max Hp </li>
 * <li> Attack stats </li>
 * <li> Heal rate </li>
 * <li> Ideal environment </li>
 * <li> Price (Buy / cost) </li>
 * <li> Icon for the monster </li>
 * </ul>
 */
public final class DetailedMonsterPanel {
    /**
     * The display panel
     */
    private JPanel monsterDisplayPanel;

    /**
     * The monster name label
     * <p>
     * Text format: "<code>{Monster::getName} ({Monster::monsterType})</code>"
     */
    private JLabel monsterNameLabel;

    /**
     * The monster description label.
     * <p>
     * Text format: "<code>{Monster::description}</code>"
     */
    private JLabel monsterDescriptionLabel;

    /**
     * The monster level label
     * <p>
     * Text format: "<code>Lv. {Monster::getLevel}</code>"
     */
    private JLabel levelLabel;

    /**
     * The monster current hp label
     * <p>
     * Text format: "<code>HP: {Monster::getCurrentHp} / {Monster::maxHp}</code>"
     */
    private JLabel monsterCurrHpLabel;

    /**
     * The monster price label
     * <p>
     * Text format: "<code>{showBuying ? Buy : Sell} price: {showBuying ? Monster::buyPrice : Monster::sellPrice}</code>"
     */
    private JLabel priceLabel;

    /**
     * The monster attack stats label
     * <p>
     * Text format: "<code>Attack: {Monster::scaledDamage}</code>"
     */
    private JLabel attackLabel;

    /**
     * The monster speed stats label
     * <p>
     * Text format: "<code>Speed: {Monster::speed}</code>"
     */
    private JLabel speedLabel;

    /**
     * The monster heal rate label
     * <p>
     * Text format: "<code>Heal rate: {Monster::healRate}</code>"
     */
    private JLabel healRateLabel;

    /**
     * The monster ideal environement label
     * <p>
     * Text format: "<code>Environment: {Monster::idealEnvironment}</code>"
     */
    private JLabel envLabel;

    /**
     * The icon for the monster type
     */
    private JLabel iconLabel;

    /**
     * The monster to be displayed
     */
    private Monster monster;

    /**
     * True if the monster should show buying cost, otherwise selling price is displayed.
     * <p>
     * Defaults to <code>true</code>
     */
    private final boolean showBuying;


    /**
     * Creates a JPanel that display all the properties of a monster
     * @param monster The monster to be displayed
     */
    public DetailedMonsterPanel(Monster monster) {
        this.monster = monster;
        this.showBuying = true;
        render();
    }

    /**
     * Creates a JPanel that display all the properties of a monster
     * @param monster The monster to be displayed
     * @param showBuying True if the sell price properties need to replaced with buy price
     */
    public DetailedMonsterPanel(Monster monster, boolean showBuying) {
        this.monster = monster;
        this.showBuying = showBuying;
        render();
    }

    /**
     * Set the boundary / location of the panel on the screen
     *
     * @param x The horizontal location
     * @param y The vertical location
     */
    public void setBounds(int x, int y) {
        monsterDisplayPanel.setBounds(x, y, WIDTH, HEIGHT);
        monsterDisplayPanel.setLayout(null);
    }

    /**
     * Add this panel to the window frame
     *
     * @param frame The JFrame to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToFrame(JFrame frame) {
        frame.getContentPane().add(monsterDisplayPanel);
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
        monsterNameLabel.setText(
            String.format("%s (%s)", monster.getName(), monster.monsterType())
        );

        monsterDescriptionLabel.setText(
            monster.description()
        );

        levelLabel.setText(
            String.format("Lv. %d", monster.getLevel())
        );

        monsterCurrHpLabel.setText(
            String.format("%d/%d", monster.getCurrentHp(), monster.maxHp())
        );
        monsterCurrHpLabel.setForeground(hpColor());

        priceLabel.setText(
            String.format("%s price: %d Gold",
                showBuying ? "Buy" : "Sell",
                showBuying ? monster.buyPrice() : monster.sellPrice()
            )
        );

        attackLabel.setText(
            String.format("Attack: %d", monster.scaledDamage())
        );

        speedLabel.setText(
            String.format("Speed: %d", monster.speed())
        );

        healRateLabel.setText(
            String.format("Heal rate: %d", monster.healRate())
        );

        envLabel.setText(
            String.format("Environment: %s", monster.idealEnvironment().toString())
        );

        iconLabel.setIcon(new ImageIcon(
            Objects.requireNonNull(DetailedMonsterPanel.class.getResource(
                String.format("/images/%s.gif", monster.monsterType().toLowerCase())
            ))
        ));
    }

    /**
     * Initialize the UI element for this panel
     */
    private void render() {
        monsterDisplayPanel = new JPanel();
        monsterDisplayPanel.setBackground(new Color(255, 250, 240));

        monsterNameLabel = new JLabel(
            String.format("%s (%s)", monster.getName(), monster.monsterType())
        );
        monsterNameLabel.setFont(new Font("Lucida Grande", Font.BOLD, 16));
        monsterNameLabel.setForeground(Color.BLACK);
        monsterNameLabel.setBounds(6, 6, 240, 36);
        monsterDisplayPanel.add(monsterNameLabel);

        monsterDescriptionLabel = new JLabel(
                monster.description()
        );
        monsterDescriptionLabel.setFont(new Font("Lucida Grande", Font.ITALIC, 11));
        monsterDescriptionLabel.setForeground(Color.BLACK);
        monsterDescriptionLabel.setBounds(6, 30, 294, 24);
        monsterDisplayPanel.add(monsterDescriptionLabel);

        JLabel hpLabel = new JLabel("HP:");
        hpLabel.setForeground(Color.BLACK);
        hpLabel.setBounds(16, 60, 25, 16);
        monsterDisplayPanel.add(hpLabel);

        levelLabel = new JLabel(
            String.format("Lv. %d", monster.getLevel())
        );
        levelLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        levelLabel.setForeground(Color.BLACK);
        levelLabel.setFont(new Font("Lucida Grande", Font.BOLD, 12));
        levelLabel.setBounds(249, 6, 45, 36);
        monsterDisplayPanel.add(levelLabel);

        monsterCurrHpLabel = new JLabel(
            String.format("%d/%d", monster.getCurrentHp(), monster.maxHp())
        );
        monsterCurrHpLabel.setForeground(hpColor());
        monsterCurrHpLabel.setBounds(44, 60, 99, 16);
        monsterDisplayPanel.add(monsterCurrHpLabel);

        priceLabel = new JLabel(
            String.format("%s price: %d Gold",
                showBuying ? "Buy" : "Sell",
                showBuying ? monster.buyPrice() : monster.sellPrice()
            )
        );
        priceLabel.setForeground(Color.BLACK);
        priceLabel.setBounds(16, 88, 127, 16);
        monsterDisplayPanel.add(priceLabel);

        attackLabel = new JLabel(
            String.format("Attack: %d", monster.scaledDamage())
        );
        attackLabel.setForeground(Color.BLACK);
        attackLabel.setBounds(16, 116, 127, 16);
        monsterDisplayPanel.add(attackLabel);

        speedLabel = new JLabel(
            String.format("Speed: %d", monster.speed())
        );
        speedLabel.setForeground(Color.BLACK);
        speedLabel.setBounds(16, 144, 127, 16);
        monsterDisplayPanel.add(speedLabel);

        healRateLabel = new JLabel(
            String.format("Heal rate: %d", monster.healRate())
        );
        healRateLabel.setForeground(Color.BLACK);
        healRateLabel.setBounds(16, 172, 127, 16);
        monsterDisplayPanel.add(healRateLabel);

        envLabel = new JLabel(
            String.format("Environment: %s", monster.idealEnvironment().toString())
        );
        envLabel.setForeground(Color.BLACK);
        envLabel.setBounds(16, 200, 127, 16);
        monsterDisplayPanel.add(envLabel);

        iconLabel = new JLabel("");
        iconLabel.setIcon(new ImageIcon(
            Objects.requireNonNull(this.getClass().getResource(
                String.format("/images/%s.gif", monster.monsterType().toLowerCase())
            ))
        ));
        iconLabel.setBounds(148, 60, 146, 156);
        monsterDisplayPanel.add(iconLabel);
    }

    /**
     * Compute the HP coloring based on the percentage of current hp to max hp
     *
     * @return Green if 50% or above, Orange if 49% to 25%, otherwise Red
     */
    private Color hpColor() {
        final var percentage = monster.getCurrentHp() * 100 / monster.maxHp();
        if (percentage < 25)
            return new Color(112, 0, 0);
        if (percentage < 50)
            return new Color(196, 120, 0);
        return new Color(46, 139, 87);
    }

    /**
     * The width of the panel
     */
    public static final int WIDTH = 300;

    /**
     * The height of the panel
     */
    public static final int HEIGHT = 251;
}
