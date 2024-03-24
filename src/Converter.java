import java.util.Random;

public class Converter {
    private String text;
    private int salt;
    private int interactionSalt;
    Random rand = new Random();

    public Converter(String text, int salt, int interactionSalt) {
        this.text = text;
        this.salt = salt;
        this.interactionSalt = interactionSalt;
    }

    public Converter(String text) {
        this.text = text;
        this.salt = rand.nextInt(256);
        this.interactionSalt = rand.nextInt(10);
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

    public boolean convert() {
        String text = this.text + "\0";

        int[] size = calculateSize(text.length());
        ImageWriter imageWriter = new ImageWriter(size[0], size[1], "output.png");

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

                    int r = (int) (decimal1 / 7.0 * 255) + this.salt + this.interactionSalt * (i + j);
                    int g = (int) (decimal2 / 7.0 * 255) + this.salt + this.interactionSalt * (i + j);
                    int b = (int) (decimal3 / 3.0 * 255) + this.salt + this.interactionSalt * (i + j);

                    imageWriter.setPixel(i, j, adjustColor(r), adjustColor(g), adjustColor(b));
                }
            }
        }

        imageWriter.setPixel(size[0] - 1, size[1] - 1, this.salt, this.interactionSalt, adjustColor(this.salt + this.interactionSalt));

        return imageWriter.create();
    }

    public int[] calculateSize(int length) {
        int width = (int) Math.ceil(Math.sqrt(length));
        int height = (int) Math.ceil((double) length / width);
        return new int[]{width, height};
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
