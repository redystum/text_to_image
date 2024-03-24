package App;

import App.Ui.FileUploadPanel;
import App.Ui.ImagePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    private JPanel Jpanel1;
    private JTextArea TextArea1;
    private JButton GenerateBtn;
    private ImagePanel imagePanel1;
    private JButton ExportBtn;
    private FileUploadPanel fileUploadPanel1;

    public MainUI() {
        createUIComponents();
        GenerateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Converter converter = new Converter(TextArea1.getText());
                boolean converted = converter.convert();
                if (!converted) {
                    JOptionPane.showMessageDialog(Jpanel1, "Failed to convert text to image!");
                    return;
                }
                System.out.println("Text has been converted to an image!");

                String inputPath = converter.getOutputPath();

                imagePanel1.updateImage(inputPath);
                // Manually set the size of the panel
                imagePanel1.setPreferredSize(new Dimension(converter.getWidth(), converter.getHeight()));
                imagePanel1.setSize(new Dimension(converter.getWidth(), converter.getHeight()));

                // Ensure the panel's size and layout are updated
                imagePanel1.revalidate();
                imagePanel1.repaint();
            }
        });
    }


    private void createUIComponents() {
        imagePanel1 = new ImagePanel();
        fileUploadPanel1 = new FileUploadPanel(this);
    }

    public void showUI() {
        JFrame frame = new JFrame("Text to Image Converter");
        frame.setContentPane(Jpanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        MainUI mainUI = new MainUI();
        mainUI.showUI();
    }

    public void updateTextAreaWithFileContent(String content) {
        TextArea1.setText(content);
    }

}
