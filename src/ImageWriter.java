import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageWriter {

    private final BufferedImage image;
    private int width;
    private int height;
    private String outputPath;

    public ImageWriter(int width, int height, String outputPath) {
        this.width = width;
        this.height = height;
        this.outputPath = outputPath;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public boolean create() {
        try {
            File outputImageFile = new File(outputPath);
            ImageIO.write(image, "png", outputImageFile);

            System.out.println("Image has been saved to: " + outputPath);
            return true;
        } catch (IOException e) {
            System.out.println("Error writing the image: " + e.getMessage());
            return false;
        }
    }

    public void setPixel(int x, int y, int r, int g, int b) {
        int rgb = (r << 16) | (g << 8) | b;
        image.setRGB(x, y, rgb);
    }

}
