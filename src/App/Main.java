package App;

import App.Components.FileUploadPanel;
import App.Components.ImagePanel;
import App.Models.Converter;
import App.Models.Converter_channels;
import App.Models.Decode;
import App.Models.Decode_channels;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Main {
    private String exportPath;
    private String importPath;

    private JPanel MainPanel;
    private JTabbedPane TabedPanel;
    private JPanel Tab1;
    private JPanel Tab2;
    private ImagePanel ConvertedImagePanel;
    private JTextArea InputTextArea;
    private JButton ExportImageBtn;
    private FileUploadPanel TextInputFileUploadPanel;
    private JButton EncodeBtn;
    private ImagePanel InputImagePanel;
    private FileUploadPanel ImageInputFileUploadPanel;
    private JTextArea OutputTextArea;
    private JButton DecodeBtn;
    private JButton ExportTextBtn;
    private JCheckBox useChannelsEncodeCheck;
    private JCheckBox useChannelsDecodeCheck;

    public Main() {

        EncodeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (useChannelsEncodeCheck.isSelected()) {
                    Converter_channels converter = new Converter_channels(InputTextArea.getText());
                    converter.convert();
                    exportPath = converter.getOutputPath();
                    System.out.println("Text has been converted to an image!");

                    ConvertedImagePanel.updateImage(exportPath);
                    ConvertedImagePanel.setPreferredSize(new Dimension(converter.getWidth(), converter.getHeight()));

                    ExportImageBtn.setEnabled(true);
                } else {
                    Converter converter = new Converter(InputTextArea.getText());
                    converter.convert();
                    exportPath = converter.getOutputPath();
                    System.out.println("Text has been converted to an image!");

                    ConvertedImagePanel.updateImage(exportPath);
                    ConvertedImagePanel.setPreferredSize(new Dimension(converter.getWidth(), converter.getHeight()));

                    ExportImageBtn.setEnabled(true);
                }
            }
        });
        ExportImageBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG Images", "png");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    File source = new File(exportPath);
                    String destPath = "";
                    if (fileToSave.getAbsolutePath().endsWith(".png")) destPath = fileToSave.getAbsolutePath();
                    else destPath = fileToSave.getAbsolutePath() + ".png";

                    File dest = new File(destPath);
                    try {
                        Files.move(source.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Saved as file: " + destPath);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });
        DecodeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (useChannelsDecodeCheck.isSelected()) {
                    Decode_channels decode = new Decode_channels(importPath);
                    String output = decode.convert();
                    OutputTextArea.setText(output);

                    ExportTextBtn.setEnabled(true);
                } else {
                    Decode decode = new Decode(importPath);
                    String output = decode.convert();
                    OutputTextArea.setText(output);

                    ExportTextBtn.setEnabled(true);
                }
            }
        });
        ExportTextBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter);
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();

                    String output = new Decode(importPath).convert();

                    if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                        fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
                    }

                    try {
                        Files.write(fileToSave.toPath(), output.getBytes());
                        System.out.println("Saved as file: " + fileToSave.getAbsolutePath());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                }
            }
        });
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        Main main = new Main();
        main.showUI();
    }

    private void createUIComponents() {
        ConvertedImagePanel = new ImagePanel(Color.BLACK);
        TextInputFileUploadPanel = new FileUploadPanel(this, FileUploadPanel.FileUploadType.TEXT);

        InputImagePanel = new ImagePanel(Color.BLACK, this);
        ImageInputFileUploadPanel = new FileUploadPanel(this, FileUploadPanel.FileUploadType.IMAGE);
    }

    public void showUI() {
        JFrame frame = new JFrame("Text to Image Converter");
        frame.setContentPane(new Main().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void updateInputTextArea(String string) {
        InputTextArea.setText(string);
    }

    public void updateImageInputPath(String string) {
        ImageInputFileUploadPanel.setFilePath(string);
        this.importPath = string;
    }

    public void setInputPathText(String absolutePath) {
        InputImagePanel.updateImage(absolutePath);
        this.importPath = absolutePath;
    }
}
