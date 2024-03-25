package App.Models;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageReader {

    private final String imagePath;
    private int width;
    private int height;
    private BufferedImage image;

    public ImageReader(String imagePath) {
        this.imagePath = imagePath;
        read();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getOutputPath() {
        return imagePath;
    }

    public boolean read() {
        try {
            this.image = ImageIO.read(new File(imagePath));
            this.width = image.getWidth();
            this.height = image.getHeight();
            return true;
        } catch (IOException e) {
            System.out.println("Error reading the image: " + e.getMessage());
            return false;
        }
    }

    public int getPixel(int x, int y) {
        return image.getRGB(x, y);
    }

}
