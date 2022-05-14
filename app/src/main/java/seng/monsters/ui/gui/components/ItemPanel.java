package seng.monsters.ui.gui.components;

import seng.monsters.model.Item;
import seng.monsters.ui.gui.InventoryScreen;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * The full item display panel
 */
public final class ItemPanel {

    /**
     * The item to be displayed
     */
    private final Item item;

    /**
     * The UI panel
     */
    private JPanel itemPanel;

    /**
     * Creates a JPanel to display the item and its description
     * @param item The item to be displayed
     */
    public ItemPanel(Item item) {
        this.item = item;
        render();
    }


    /**
     * Set the boundary / location of the panel on the screen
     *
     * @param x The horizontal location
     * @param y The vertical location
     */
    public void setBounds(int x, int y) {
        itemPanel.setBounds(x, y, WIDTH, HEIGHT);
        itemPanel.setLayout(null);
    }

    /**
     * Add this panel to the window frame
     *
     * @param frame The JFrame to be displayed into (<b>Must use absolute positioning</b>)
     */
    public void applyToFrame(JFrame frame) {
        frame.getContentPane().add(itemPanel);
    }

    /**
     * Initialize the UI element for this panel
     */
    private void render() {
        itemPanel = new JPanel();
        itemPanel.setBackground(new Color(255, 250, 240));

        JLabel itemNameLabel = new JLabel(item.getName());
        itemNameLabel.setBounds(6, 6, 146, 20);
        itemPanel.add(itemNameLabel);
        itemNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        itemNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel(item.description());
        descLabel.setForeground(new Color(0, 128, 128));
        descLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        descLabel.setBounds(6, 142, 146, 20);
        itemPanel.add(descLabel);

        JLabel iconLabel = new JLabel("");
        iconLabel.setIcon(new ImageIcon(
            Objects.requireNonNull(InventoryScreen.class.getResource(
                String.format("/images/%s.png", item.getName().toLowerCase())
            ))
        ));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBounds(6, 38, 146, 92);
        itemPanel.add(iconLabel);
    }

    /**
     * The width of the panel
     */
    public static final int WIDTH = 158;

    /**
     * The height of the panel
     */
    public static final int HEIGHT = 168;
}
