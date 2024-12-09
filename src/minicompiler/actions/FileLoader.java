package minicompiler.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;

import minicompiler.ui.EditorPage;

public class FileLoader {

    private EditorPage editorPage;

    public FileLoader(EditorPage editorPage) {
        this.editorPage = editorPage;
    }

    public void openFile() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(editorPage);
        if (result == JFileChooser.APPROVE_OPTION) {
            File currentFile = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(currentFile))) {
                editorPage.getCodeTextArea().setText("");
                String line;
                while ((line = reader.readLine()) != null) {
                    editorPage.getCodeTextArea().append(line + "\n");
                }
                editorPage.getResultTextArea().setText(currentFile.getName() + " opened successfully.\n");
                editorPage.getLexicalAnalysisButton().setEnabled(true);
            } catch (IOException ex) {
                editorPage.getResultTextArea().setText("Error reading file: " + ex.getMessage());
            }
        }
    }
}