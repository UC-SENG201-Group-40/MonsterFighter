package seng.monsters.ui.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import seng.monsters.model.Item;

public final class ItemPanel {
	
	private final Item item;
	
	private JPanel itemPanel;
	
	private JLabel itemNameLabel;
	
	private JLabel descLabel;
	
	private JLabel iconLabel;
	
	public ItemPanel(Item item) {
		this.item = item;
		initialize();
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
	private void initialize() {
        itemPanel = new JPanel();
        itemPanel.setBackground(new Color(255, 250, 240));
        
        itemNameLabel = new JLabel(item.getName());
        itemNameLabel.setBounds(6, 6, 146, 20);
        itemPanel.add(itemNameLabel);
        itemNameLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
        itemNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        descLabel = new JLabel(item.description());
        descLabel.setForeground(new Color(0, 128, 128));
        descLabel.setHorizontalAlignment(SwingConstants.LEFT);
        descLabel.setFont(new Font("Lucida Grande", Font.PLAIN, 13));
        descLabel.setBounds(6, 142, 146, 20);
        itemPanel.add(descLabel);
        
        iconLabel = new JLabel("");
        iconLabel.setIcon(new ImageIcon(
        		InventoryScreen.class.getResource(
        			String.format("/images/%s.png", item.getName().toLowerCase())
        		)
        	));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBounds(6, 38, 146, 92);
        itemPanel.add(iconLabel);
	}
	
    public static final int WIDTH = 158;
    public static final int HEIGHT = 168;
}
