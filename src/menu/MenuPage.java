package menu;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.plaf.DimensionUIResource;

import components.AdvancedButton;

import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;

import window.GameWindow;

public class MenuPage extends JPanel {

    public AdvancedButton startButton;
    public AdvancedButton settingsButton;
    public AdvancedButton quitButton;

    public MenuPage() {

        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        startButton = new AdvancedButton("Start Game", 1.f);
        settingsButton = new AdvancedButton("Settings", 1.f);
        quitButton = new AdvancedButton("Quit", 1.f);

        //Use one central ActionListener in ../window/GameWindow.java
        startButton.addActionListener(GameWindow.getInstance());
        settingsButton.addActionListener(GameWindow.getInstance());
        this.add(startButton);
        this.add(settingsButton);
        this.add(quitButton);
    }
}