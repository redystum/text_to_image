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

        System.out.println("salt: " + salt);
        System.out.println("interactionSalt: " + interactionSalt);

        //pixel: 1936682341
        //pixel: 544499064
        //pixel: 1948279137
        //pixel: 1635416698
        //pixel: 1635385344
        //salt: 191
        //interactionSalt: 30
        System.out.println("left: " + left);

        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {

                if (i == height - 1 && j >= width - left) {
                    continue;
                }

                int rgb = imageReader.getPixel(j, i);

                System.out.println("rgb: " + rgb);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                int a = (rgb >> 24) & 0xFF; // alpha channel


                sb.append((char) r);
                sb.append((char) g);
                sb.append((char) b);
                sb.append((char) a);
            }
        }



//        for (int i = 0; i < width; i++) {
//            for (int j = 0; j < height; j++) {
//
//                if (i == width - 1 && j >= height - left) {
//                    continue;
//                }
//
//                int rgb = imageReader.getPixel(i, j);
//
//                int a = (rgb >> 24) & 0xFF;
//                int r = (rgb >> 16) & 0xFF;
//                int g = (rgb >> 8) & 0xFF;
//                int b = rgb & 0xFF;
//
//
////                r = adjustColor(r - salt - interactionSalt * i);
////                g = adjustColor(g - salt - interactionSalt * i);
////                b = adjustColor(b - salt - interactionSalt * i);
////                a = adjustColor(a - salt - interactionSalt * i);
//
//
//                //r: 78 g: 111 b: 32 a: 115
//                //r: 97 g: 108 b: 116 a: 32
//                //r: 101 g: 110 b: 99 a: 111
//                //r: 100 g: 101 b: 32 a: 105
//                //r: 109 g: 97 b: 103 a: 101
//                //salt: 245
//                //interactionSalt: 252
//
//                System.out.println("r: " + r + " g: " + g + " b: " + b + " a: " + a);
//
//                sb.append((char) r);
//                sb.append((char) g);
//                sb.append((char) b);
//                sb.append((char) a);
//            }
//        }

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
