package eu.bionade24.junglejump.window;

import java.awt.Window;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;

import eu.bionade24.junglejump.game.Game;
import eu.bionade24.junglejump.menu.MenuPage;
import eu.bionade24.junglejump.settings.SettingsPage;

public class GameWindow extends JFrame {

    // Var for singleton design
    private static GameWindow _instance;

    private MenuPage _menupage;
    private SettingsPage _settingspage;
    private Game _game;

    private enum WindowLayout {
        FULLSCREEN, WINDOW
    }

    public GameWindow() {

        // Use it as singleton instance
        _instance = this;

        _menupage = new MenuPage();
        this.add(_menupage);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.resizeWindow(WindowLayout.FULLSCREEN); // Use own method instead of setExtendedState() to not rely on wm
        this.setTitle("Jump'n run game");
        this.setResizable(false);
        this.setVisible(true);

        this.repaint();
    }

    // Method for singleton design
    public static GameWindow getInstance() {
        if (_instance == null)
            _instance = new GameWindow();
        return _instance;
    }

    public void resizeWindow(WindowLayout wl) {
        if (wl == WindowLayout.WINDOW) {
            this.setUndecorated(false);
        } else {
            this.setUndecorated(true);
        }

        for (Window w : Window.getWindows()) {
            GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(w);
        }
    }

    public void launchMenu() {
        if (_game != null) {
            this.remove(_game);
            _game = null;
        } else {
            if (_settingspage != null) {
                this.remove(_settingspage);
                _settingspage = null;
            } else {
                throw new RuntimeException("No initialised object for descruction avaible during launch");
            }
        }
        _menupage = new MenuPage();
        this.add(_menupage);
        this.setVisible(true);
        this.repaint();
    }

    public void launchSettings() {
    }

    public void launchGame() {
        // Doesn't need to care about _settingspage as you can't access game from
        // settings
        this.remove(_menupage);
        _game = new Game();
        this.add(_game);
        this.setVisible(true);
        this.repaint();
        _menupage = null; // Make _menupage ready for garbage collector
    }
}