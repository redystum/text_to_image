package App.Models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class Converter_channels {
    Random rand = new Random();
    private String text;
    private int salt;
    private int interactionSalt;
    private int width;
    private int height;
    private String outputPath;

    public Converter_channels(String text, int salt, int interactionSalt) {
        this.text = text;
        this.salt = salt;
        this.interactionSalt = interactionSalt;
    }

    public Converter_channels(String text) {
        this.text = text;
        this.salt = rand.nextInt(256);
        this.interactionSalt = rand.nextInt(256);
    }

    public Converter_channels(String text, boolean useSalt) {
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
        int textOriginalLenght = text.length();
        String text = this.text;

        int textSize = (int) Math.ceil((double) text.length() / 4) + 1;
        int[] size = calculateSize(textSize);

        try {
            Path tempFilePath = Files.createTempFile("output", ".png");
            this.outputPath = tempFilePath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        ImageWriter imageWriter = new ImageWriter(size[0], size[1], this.outputPath);
        int left = 0;

        // each pixel have 4 letters, each letter on a channel (rgba)
        int letterIndex = 0;
        for (int i = 0; i < size[0]; i++) {
            for (int j = 0; j < size[1]; j++) {

                int index = i * size[1] + j;
                if (index < textSize) {
                    int pixel = 0;
                    for (int k = 0; k < 4; k++) {
                        pixel = pixel << 8;
                        if (letterIndex < textOriginalLenght) {
                            pixel = pixel | text.charAt(letterIndex);
                        } else {
                            left++;
                        }
                        letterIndex++;
                    }
                    imageWriter.setPixel(i, j, (pixel >> 24) & 0xFF, (pixel >> 16) & 0xFF, (pixel >> 8) & 0xFF, pixel & 0xFF);
                } else {
                    imageWriter.setPixel(i, j, 0, 0, 0, 0);
                    left++;
                }
            }
        }

        if (left > 255) left = 255;
        System.out.println("left: " + left);

        System.out.println("salt: " + salt);
        System.out.println("interactionSalt: " + interactionSalt);


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
