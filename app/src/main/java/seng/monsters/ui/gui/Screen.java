package seng.monsters.ui.gui;

import seng.monsters.model.GameManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;

/**
 * A interface for all the GUI Window Screen
 */
public abstract class Screen {
    /**
     * The GUI manager for navigation
     */
    protected final GUI gui;

    /**
     * The game manager to perform actions onto the model and retrieve data
     */
    protected final GameManager gameManager;

    /**
     * The window screen
     */
    protected final JFrame frame;


    /**
     * Create an active GUI screen
     * @param gui The GUI manager
     * @param gameManager The Game logic manager / controller
     */
    public Screen(GUI gui, GameManager gameManager) {
        this.gui = gui;
        this.gameManager = gameManager;
        this.frame = new JFrame();
        this.frame.getContentPane().setBackground(Screen.backgroundColor());
        this.frame.getContentPane().setForeground(new Color(0, 0, 0));
        this.frame.setBounds(100, 100, Screen.WIDTH, Screen.HEIGHT);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.getContentPane().setLayout(null);
        this.frame.setResizable(false);
    }


    /**
     * Render all the Screen's content
     */
    public abstract void render();

    /**
     * Close the Screen and dispose it
     */
    public void dispose() {
        frame.dispose();
    }

    /**
     * The width of the Screen
     */
    public static int WIDTH = 819;

    /**
     * The height of the Screen
     */
    public static int HEIGHT = 487;

    /**
     * The background color for all Screen
     *
     * @return The color
     */
    public static Color backgroundColor() {
        return new Color(255, 255, 204);
    }

    /**
     * Get the image resource and return it as ImageIcon
     * @param path The path to resource
     * @return ImageIcon from the image if any
     */
    public static Optional<ImageIcon> imageIconFromResource(String path) {
        try {
            final URL url = Screen.class.getResource(path);
            if (url != null)
                return Optional.of(new ImageIcon(url));
            final InputStream stream = Screen.class.getResourceAsStream(path);
            if (stream != null)
                return Optional.of(new ImageIcon(ImageIO.read(stream)));
            return Optional.empty();
        } catch (java.io.IOException | NullPointerException ignored) {
            return Optional.empty();
        }
    }
}
