package eu.bionade24.junglejump.components;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.JPEGTranscoder;
import org.imgscalr.*;

public class Helper {
    public static BufferedImage getImage(String path, int wantedWidth) {
        BufferedImage image;
        try {
            image = ImageIO.read(ClassLoader.getSystemResourceAsStream(path));
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
        return Scalr.resize(image, Scalr.Method.BALANCED, wantedWidth);
    }

    public static BufferedImage getVectorGraphic(String path, float wantedWidth, float wantedHeight) {
        BufferedImageTranscoder imageTranscoder = new BufferedImageTranscoder();

        imageTranscoder.addTranscodingHint(JPEGTranscoder.KEY_WIDTH, wantedWidth);
        imageTranscoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, wantedHeight);

        TranscoderInput input = new TranscoderInput(ClassLoader.getSystemResourceAsStream(path));
        try {
            imageTranscoder.transcode(input, null);
        } catch (TranscoderException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return imageTranscoder.getBufferedImage();
    }

    protected static class BufferedImageTranscoder extends ImageTranscoder {
        @Override
        public BufferedImage createImage(int w, int h) {
            BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            return bi;
        }

        @Override
        public void writeImage(BufferedImage img, TranscoderOutput output) throws TranscoderException {
            this.img = img;
        }

        public BufferedImage getBufferedImage() {
            return img;
        }

        private BufferedImage img = null;
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