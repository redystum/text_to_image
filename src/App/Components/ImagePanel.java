package App.Components;

import App.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImagePanel extends JPanel {
    private Image image;
    private Main main;

    public ImagePanel(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImagePanel(Color color, Main main) {
        this.main = main;
        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 400, 400);
        g.dispose();
        repaint();

        setDropTarget(new FileDropTarget());
    }

    public ImagePanel(Color color) {
        image = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);

        Graphics g = image.getGraphics();
        g.setColor(color);
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
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }


    private class FileDropTarget extends DropTarget {
        @Override
        public synchronized void drop(DropTargetDropEvent evt) {
            try {
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = evt.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    java.util.List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (files.size() > 0) {
                        File droppedFile = files.get(0);
                        main.updateImageInputPath(droppedFile.getAbsolutePath());
                        updateImage(droppedFile.getAbsolutePath());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
