package App.Ui;

import App.Converter;

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
        GenerateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Converter converter = new Converter(TextArea1.getText());
                converter.convert();
                String outputPath = converter.getOutputPath();
                System.out.println("Text has been converted to an image!");

                imagePanel1.updateImage(outputPath);
                imagePanel1.setPreferredSize(new Dimension(converter.getWidth(), converter.getHeight()));

            }
        });
    }

    public static void main(String[] args) {
        MainUI mainUI = new MainUI();
        mainUI.showUI();
    }

    private void createUIComponents() {
        imagePanel1 = new ImagePanel();
        fileUploadPanel1 = new FileUploadPanel(this);
    }

    public void showUI() {
        JFrame frame = new JFrame("Text to Image Converter");
        frame.setContentPane(new MainUI().Jpanel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void updateTextAreaWithFileContent(String string) {
        TextArea1.setText(string);
    }
}
