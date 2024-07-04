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
                int[] shifts = {16, 8, 0, 24};
                int[] channelValues = new int[4];

                for (int k = 0; k < 4; k++) {
                    if (current < stringSize) {
                        channelValues[k] = (rgb >> shifts[k]) & 0xFF;
                        channelValues[k] = adjustColor(channelValues[k] - this.salt - this.interactionSalt * (i * j));
                        sb.append((char) channelValues[k]);
                    }
                    current++;
                }

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
