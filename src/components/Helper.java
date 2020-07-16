package components;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.event.ActionEvent;

import org.imgscalr.*;

public class Helper {
    public static BufferedImage getImage(String path, int wantedWidth) {
        BufferedImage image;
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
        return Scalr.resize(image, Scalr.Method.BALANCED, wantedWidth);
    }

    public static Action actionFactory(Consumer<ActionEvent> actionPerformed) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionPerformed.accept(e);
            }
        };
    };
}