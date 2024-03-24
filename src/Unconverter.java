public class Unconverter {

    private final ImageReader imageReader;
    private final int width;
    private final int height;
    private final int left;
    private int salt;
    private int interactionSalt;

    public Unconverter(String imagePath) {
        this.imageReader = new ImageReader(imagePath);
        this.width = imageReader.getWidth();
        this.height = imageReader.getHeight();
        int[] salts = getSalts();
        this.salt = salts[0];
        this.interactionSalt = salts[1];
        this.left = getLeft();
    }

    public Unconverter(String imagePath, int salt, int interactionSalt) {
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

    public String unconvert() {
        if (!imageReader.read()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {

                if (i == width - 1 && j >= height - left) {
                    continue;
                }

                int rgb = imageReader.getPixel(i, j);

                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                r = adjustColor(r - salt - interactionSalt * (i * j));
                g = adjustColor(g - salt - interactionSalt * (i * j));
                b = adjustColor(b - salt - interactionSalt * (i * j));

                if (r == 0 && g == 0 && b == 0) {
                    continue;
                }

                int decimal1 = Math.round(r / 255.0f * 7);
                int decimal2 = Math.round(g / 255.0f * 7);
                int decimal3 = Math.round(b / 255.0f * 3);
                String binary = String.format("%3s", Integer.toBinaryString(decimal1)).replace(' ', '0') +
                        String.format("%3s", Integer.toBinaryString(decimal2)).replace(' ', '0') +
                        String.format("%2s", Integer.toBinaryString(decimal3)).replace(' ', '0');

                sb.append((char) Integer.parseInt(binary, 2));
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
