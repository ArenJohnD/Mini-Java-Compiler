package minicompiler.main;

import javax.swing.SwingUtilities;

import minicompiler.ui.WelcomePage;

public class MiniJavaCompiler {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomePage().setVisible(true);
        });
    }
}