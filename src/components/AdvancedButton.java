package components;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
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
            try {
                buttonImage = ImageIO.read(new File("graphics/Button.png"));
            } catch (IOException e) {
                System.err.println(e);
            }
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