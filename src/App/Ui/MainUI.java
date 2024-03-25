package App.Ui;

import App.Converter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MainUI {
    private JPanel Jpanel1;
    private JTextArea TextArea1;
    private JButton GenerateBtn;
    private ImagePanel imagePanel1;
    private JButton ExportBtn;
    private FileUploadPanel fileUploadPanel1;
    private JTabbedPane tabbedPane1;
    private String exportPath;

    public MainUI() {
        GenerateBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Converter converter = new Converter(TextArea1.getText());
                converter.convert();
                exportPath = converter.getOutputPath();
                System.out.println("Text has been converted to an image!");

                imagePanel1.updateImage(exportPath);
                imagePanel1.setPreferredSize(new Dimension(converter.getWidth(), converter.getHeight()));

                ExportBtn.setEnabled(true);
            }
        });
        ExportBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    File source = new File(exportPath);
                    String destPath = "";
                    if (fileToSave.getAbsolutePath().endsWith(".png"))
                        destPath = fileToSave.getAbsolutePath();
                    else
                        destPath = fileToSave.getAbsolutePath() + ".png";

                    File dest = new File(destPath);
                    try {
                        Files.move(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Saved as file: " + fileToSave.getAbsolutePath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

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
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateTextAreaWithFileContent(String string) {
        TextArea1.setText(string);
    }
}
