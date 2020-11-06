package eu.bionade24.junglejump.components;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import static org.lwjgl.system.MemoryStack.*;
import java.nio.ByteBuffer;
import org.lwjgl.nanovg.NSVGImage;
import static org.lwjgl.nanovg.NanoSVG.*;


//Currently unneeded due to parsing problems with certain SVG files, works generally
public class ImageRasterizer {

    long rasterizer;

    public ImageRasterizer() {
        try (MemoryStack stack = stackPush()) {
            rasterizer = nsvgCreateRasterizer();
        }
    }

    public PixelObject readImage(String path) {
        try (MemoryStack stack = stackPush()) {
            NSVGImage image = nsvgParseFromFile(Helper.resolvePath("graphics/jungle.svg"), "px", 96);
            if (image == null) {
                throw new RuntimeException("Image failed to load: " + path);
            }
            int width = (int) image.width();
            int height = (int) image.height();
            System.out.println("\n" + image.width() + " " + image.height() + "\n");
            float xOffset = 0.f;
            float yOffset = 0.f;
            float scale = 1.f;
            int stride = (int) (width * 4);
            ByteBuffer pixels = MemoryUtil.memAlloc((int) (stride * height));

            nsvgRasterize(rasterizer, image, xOffset, yOffset, scale, pixels, width, height, stride);
            nsvgDelete(image);
            if (pixels == null) {
                throw new RuntimeException("Pixels from image failed to load: " + path);
            }
            PixelObject obj = new PixelObject(pixels, width, height, stride);
            return obj;
        }
    }

    protected void finalize() {
        try (MemoryStack stack = stackPush()) {
            nsvgDeleteRasterizer(rasterizer);
        }
    }

    public class PixelObject {
        public ByteBuffer pixels;
        public int width;
        public int height;
        public int stride;

        protected PixelObject(ByteBuffer p, int w, int h, int s) {
            pixels = p;
            width = w;
            height = h;
            stride = s;
        }

        protected void finalize() {
            MemoryUtil.memFree(pixels);
        }
    }
}
