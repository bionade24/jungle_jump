package eu.bionade24.junglejump.menu;

import java.awt.*;

import javax.swing.JPanel;
import eu.bionade24.junglejump.components.AdvancedButton;
import eu.bionade24.junglejump.components.Helper;

import javax.swing.JButton;

import eu.bionade24.junglejump.window.GameWindow;

public class MenuPage extends JPanel {

    private AdvancedButton startButton;
    private AdvancedButton settingsButton;
    private AdvancedButton quitButton;

    private static Color backgroundColor = new Color(255, 190, 30);

    public MenuPage() {

        this.setBackground(backgroundColor);

        startButton = new AdvancedButton("Start Game");
        settingsButton = new AdvancedButton("Settings");
        quitButton = new AdvancedButton("Quit");

        startButton.addActionListener(Helper.actionFactory(a -> GameWindow.getInstance().launchGame()));
        settingsButton.addActionListener(Helper.actionFactory(a -> GameWindow.getInstance().launchSettings()));
        quitButton.addActionListener(Helper.actionFactory(a -> GameWindow.getInstance().dispose()));

        this.setLayout(new GridLayout(1, 3));
        JButton t1 = new JButton();
        t1.setVisible(false);
        this.add(t1);

        JPanel p2 = new JPanel();
        p2.setBackground(backgroundColor);
        p2.setLayout(new GridLayout(3, 1, 0, 20));
        p2.add(startButton);
        p2.add(settingsButton);
        p2.add(quitButton);

        this.add(p2);

        JButton t2 = new JButton();
        t2.setVisible(false);

        this.add(t2);
    }
}