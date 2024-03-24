package App.Ui;

import javax.swing.*;
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

public class FileUploadExample extends JFrame {

    private JTextField filePathTextField;

    public FileUploadExample() {
        super("File Upload Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 150);

        // Layout
        JPanel panel = new JPanel(new BorderLayout());
        JButton browseButton = new JButton("Browse");
        filePathTextField = new JTextField(20);
        filePathTextField.setDropTarget(new FileDropTarget());

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathTextField.setText(selectedFile.getAbsolutePath());
                    readFile(selectedFile);
                }
            }
        });

        panel.add(filePathTextField, BorderLayout.CENTER);
        panel.add(browseButton, BorderLayout.EAST);

        getContentPane().add(panel);
        setVisible(true);
    }

    private void readFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            // Aqui você pode fazer o que quiser com o conteúdo do arquivo,
            // como exibir em um JTextArea ou processá-lo de outra forma
            System.out.println("Conteúdo do arquivo:\n" + content.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                        File droppedFile = files.get(0); // Only handle the first dropped file
                        filePathTextField.setText(droppedFile.getAbsolutePath());
                        readFile(droppedFile);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileUploadExample();
            }
        });
    }
}
