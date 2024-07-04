package App.Models;

public class Decode_channels {

    private final ImageReader imageReader;
    private final int width;
    private final int height;
    private final int left;
    private int salt;
    private int interactionSalt;

    public Decode_channels(String imagePath) {
        this.imageReader = new ImageReader(imagePath);
        this.width = imageReader.getWidth();
        this.height = imageReader.getHeight();
        int[] salts = getSalts();
        this.salt = salts[0];
        this.interactionSalt = salts[1];
        this.left = getLeft();
    }

    public Decode_channels(String imagePath, int salt, int interactionSalt) {
        this.imageReader = new ImageReader(imagePath);
        this.width = imageReader.getWidth();
        this.height = imageReader.getHeight();
        this.salt = salt;
        this.interactionSalt = interactionSalt;
        this.left = getLeft();
    }

    public void setSalts(int salt, int interactionSalt) {
        this.salt = salt;
        this.interactionSalt = interactionSalt;
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

    public int getLeft() {
        int rgb = imageReader.getPixel(width - 1, height - 1);
        return rgb & 0xFF;
    }

    public int[] getSalts() {
        int rgb = imageReader.getPixel(width - 1, height - 1);
        int r = (rgb >> 16) & 0xFF;
        int g = (rgb >> 8) & 0xFF;

        return new int[]{r, g};
    }

    public String convert() {
        if (!imageReader.read()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        int totalSize = (width * height * 4);
        int stringSize = totalSize - left;

        int current = 0;
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {

                int rgb = imageReader.getPixel(j, i);

                int r = 0, g = 0, b = 0, a = 0;

                System.out.println("rgb: " + rgb);
                if (current < stringSize) {
                    r = (rgb >> 16) & 0xFF;
                    r = adjustColor(r - this.salt - this.interactionSalt * (i * j));
                    System.out.println("r: " + r + (char) r);
                    sb.append((char) r);
                }
                current++;

                if (current < stringSize) {
                    g = (rgb >> 8) & 0xFF;
                    g = adjustColor(g - this.salt - this.interactionSalt * (i * j));
                    sb.append((char) g);
                }
                current++;

                if (current < stringSize) {
                    b = rgb & 0xFF;
                    b = adjustColor(b - this.salt - this.interactionSalt * (i * j));
                    sb.append((char) b);
                }
                current++;

                if (current < stringSize) {
                    a = (rgb >> 24) & 0xFF;
                    a = adjustColor(a - this.salt - this.interactionSalt * (i * j));
                    sb.append((char) a);
                }
                current++;

            }
        }

        return sb.toString();
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
