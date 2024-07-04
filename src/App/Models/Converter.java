package App.Models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Converter {
    Random rand = new Random();
    private String text;
    private int salt;
    private int interactionSalt;
    private int width;
    private int height;
    private String outputPath;

    public Converter(String text, int salt, int interactionSalt) {
        this.text = text;
        this.salt = salt;
        this.interactionSalt = interactionSalt;
    }

    public Converter(String text) {
        this.text = text;
        this.salt = rand.nextInt(256);
        this.interactionSalt = rand.nextInt(256);
    }

    public Converter(String text, boolean useSalt) {
        this.text = text;
        this.salt = useSalt ? rand.nextInt(256) : 0;
        this.interactionSalt = useSalt ? rand.nextInt(10) : 0;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSalt() {
        return salt;
    }

    public void setSalt(int salt) {
        this.salt = salt;
    }

    public int getInteractionSalt() {
        return interactionSalt;
    }

    public void setInteractionSalt(int interactionSalt) {
        this.interactionSalt = interactionSalt;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }


    public boolean convert() {
        String text = this.text + "\0";

        int[] size = calculateSize(text.length());

        try {
            Path tempFilePath = Files.createTempFile("output", ".png");
            this.outputPath = tempFilePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        ImageWriter imageWriter = new ImageWriter(size[0], size[1], this.outputPath);

        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {
                if (i * size[1] + j < text.length()) {
                    char c = text.charAt(i * size[1] + j);

                    String binary = Integer.toBinaryString(c);
                    binary = String.format("%8s", binary).replace(' ', '0');

                    String part1 = binary.substring(0, 3);
                    String part2 = binary.substring(3, 6);
                    String part3 = binary.substring(6, 8);

                    int decimal1 = Integer.parseInt(part1, 2);
                    int decimal2 = Integer.parseInt(part2, 2);
                    int decimal3 = Integer.parseInt(part3, 2);

                    int r = (int) (decimal1 / 7.0 * 255) + this.salt + this.interactionSalt * (i * j);
                    int g = (int) (decimal2 / 7.0 * 255) + this.salt + this.interactionSalt * (i * j);
                    int b = (int) (decimal3 / 3.0 * 255) + this.salt + this.interactionSalt * (i * j);

                    imageWriter.setPixel(i, j, adjustColor(r), adjustColor(g), adjustColor(b));
                } else {
                    int r = rand.nextInt(256);
                    int g = rand.nextInt(256);
                    int b = rand.nextInt(256);
                    imageWriter.setPixel(i, j, r, g, b);
                }
            }
        }

        int left = size[0] * size[1] - text.length();
        if (left > 255) left = 255;

        imageWriter.setPixel(size[0] - 1, size[1] - 1, this.salt, this.interactionSalt, left);

        return imageWriter.create();
    }

    public int[] calculateSize(int length) {
        this.width = (int) Math.ceil(Math.sqrt(length));
        this.height = (int) Math.ceil((double) length / this.width);

        return new int[]{this.width, this.height};
    }

    public int adjustColor(int color) {
        while (color < 0) {
            color += 256;
        }
        while (color >= 256) {
            color -= 256;
        }
        return color;
    }
}
