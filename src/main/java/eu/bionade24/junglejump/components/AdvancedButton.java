package eu.bionade24.junglejump.components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import javax.swing.JButton;

import eu.bionade24.junglejump.window.GameWindow;

import javax.swing.BorderFactory;

public class AdvancedButton extends JButton {

    private static BufferedImage buttonImage;
    private String label;

    public AdvancedButton(String label) {
        this.label = label;
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setContentAreaFilled(false);
        loadImage();
    }

    private static void loadImage() { // Use one static instance for better performance
        if (buttonImage == null) {
            buttonImage = Helper.getVectorGraphic("graphics/button.svg", 939.f, 417.f);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(buttonImage, 0, 0, this.getWidth(), this.getHeight(), null);
        g2d.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
        g2d.drawString(this.label, this.getWidth() / 3, this.getHeight() / 2);
    }

    @Override
    public void setPreferredSize(Dimension d) {
        super.setPreferredSize(d);
        this.repaint();
    }
}
