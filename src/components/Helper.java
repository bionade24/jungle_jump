package components;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
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
}