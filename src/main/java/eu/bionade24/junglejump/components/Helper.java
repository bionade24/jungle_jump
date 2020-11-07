package eu.bionade24.junglejump.components;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.function.Consumer;
import static java.lang.ClassLoader.getSystemClassLoader;

import javax.swing.AbstractAction;
import javax.swing.Action;

public class Helper {

    public static String resolvePath(String path) {
        try {
            return Paths.get(new URI(getSystemClassLoader().getResource(path).toExternalForm())).toString();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException("Path not resolvable: " + path);
        }
    }
    public static BufferedImage getImage(String path, int wantedWidth) {
        return null;
    }

    public static BufferedImage getVectorGraphic(String path, float wantedWidth, float wantedHeight) {
        return null;
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