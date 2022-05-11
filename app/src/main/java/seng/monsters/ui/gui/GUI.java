package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;
import seng.monsters.model.Item;
import seng.monsters.model.Monster;

/**
 * The GUI class manager to handle window navigation and closing
 */
public final class GUI {
    /**
     * The GameManager for the GUI
     */
    private final GameManager gameManager;

    /**
     * Currently active screen
     */
    private Screen activeScreen;

    public GUI() {
        gameManager = new GameManager();
        activeScreen = new TitleScreen(this, gameManager);
        activeScreen.render();
    }

    /**
     * Navigate to a new Java Swing Screen.
     * <p>
     * This will close the currently active screen and replace it with the new one.
     * <p>
     * If you are planning to display a pop out, just display the JFrame normally
     *
     * @param screen The UI Screen to be displayed
     */
    public void navigateTo(Screen screen) {
        final var oldScreen = activeScreen;
        activeScreen = screen;
        activeScreen.render();
        oldScreen.dispose();
    }

    /**
     * Close the active screen and end the entire application (no screen left)
     */
    public void quit() {
        activeScreen.dispose();
    }

    /**
     * Navigate back to main menu
     */
    public void navigateBackToMainMenu() {
        navigateTo(new MainMenuScreen(this, gameManager));
    }
}
