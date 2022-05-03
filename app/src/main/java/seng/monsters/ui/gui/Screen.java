package seng.monsters.ui.gui;

/**
 * A interface for all the GUI Window Screen
 */
public interface Screen {
	/**
	 * Initialize the Screen and all its content
	 */
	public void initialize();
	
	/**
	 * Close the Screen and dispose it
	 */
	public void dispose();
}
