package menu;

import java.awt.*;
import javax.swing.JPanel;
import components.AdvancedButton;
import javax.swing.JButton;

import window.GameWindow;

public class MenuPage extends JPanel {

    public AdvancedButton startButton;
    public AdvancedButton settingsButton;
    public AdvancedButton quitButton;

    public MenuPage() {

        startButton = new AdvancedButton("Start Game");
        settingsButton = new AdvancedButton("Settings");
        quitButton = new AdvancedButton("Quit");

        // Use one central ActionListener in ../window/GameWindow.java
        startButton.addActionListener(GameWindow.getInstance());
        settingsButton.addActionListener(GameWindow.getInstance());
        quitButton.addActionListener(GameWindow.getInstance());

        this.setLayout(new GridLayout(1, 3));
        JButton t1 = new JButton();
        t1.setVisible(false);
        this.add(t1);

        JPanel p2 = new JPanel();
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