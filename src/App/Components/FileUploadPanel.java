package App.Components;

import App.Main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class FileUploadPanel extends JPanel {

    private final JTextField filePathTextField;
    private final Main main;
    private final FileUploadType type;

    public FileUploadPanel(Main main, FileUploadType type) {
        this.main = main;
        this.type = type;
        setLayout(new BorderLayout());

        JButton browseButton = new JButton("Browse");
        filePathTextField = new JTextField(20);
        filePathTextField.setDropTarget(new FileDropTarget());

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = null;
                if (type == FileUploadType.IMAGE) {
                    filter = new FileNameExtensionFilter("PNG Images", "png");
                } else if (type == FileUploadType.TEXT) {
                    filter = new FileNameExtensionFilter("Text Files", "txt");
                }
                fileChooser.setFileFilter(filter);
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();

                    if (type == FileUploadType.IMAGE) {
                        filePathTextField.setText(selectedFile.getAbsolutePath());
                        main.setInputPathText(selectedFile.getAbsolutePath());
                    } else if (type == FileUploadType.TEXT) {
                        filePathTextField.setText(selectedFile.getAbsolutePath());
                        readFile(selectedFile);
                    }
                }
            }
        });

        add(filePathTextField, BorderLayout.CENTER);
        add(browseButton, BorderLayout.EAST);
    }

    public void setFilePath(String path) {
        filePathTextField.setText(path);
    }

    private void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            main.updateInputTextArea(content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public enum FileUploadType {
        IMAGE, TEXT
    }

    private class FileDropTarget extends DropTarget {
        @Override
        public synchronized void drop(DropTargetDropEvent evt) {
            try {
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = evt.getTransferable();
                if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                    List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                    if (files.size() > 0) {
                        File droppedFile = files.get(0);
                        filePathTextField.setText(droppedFile.getAbsolutePath());

                        if (type == FileUploadType.IMAGE) {
                            main.setInputPathText(droppedFile.getAbsolutePath());
                        } else if (type == FileUploadType.TEXT) {
                            readFile(droppedFile);
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
