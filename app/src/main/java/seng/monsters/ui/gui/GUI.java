package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;

public final class GUI {
	private final GameManager gameManager;
	private Screen activeScreen;
	
	public GUI() {
		gameManager = new GameManager();
		activeScreen = new TitleScreen(this, gameManager);
		activeScreen.initialize();
	}
	
	public void navigateTo(Screen screen) {
		final var oldScreen = activeScreen;
		activeScreen = screen;
		activeScreen.initialize();
		oldScreen.dispose();
	}
	
	public void quit() {
		activeScreen.dispose();
	}
}
