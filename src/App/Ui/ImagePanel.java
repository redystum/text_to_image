package App.Ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImagePanel() {
        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 400, 400);
        g.dispose();

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public void updateImage(String imagePath) {
        new Thread(() -> {
            try {
                image = ImageIO.read(new File(imagePath));
                System.out.println("Image has been updated to: " + imagePath);
                System.out.println("Width: " + image.getWidth(null));
                SwingUtilities.invokeLater(() -> {
                    repaint();
                    revalidate();
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }

}
