package main;

import javax.swing.SwingUtilities;

import ui.WelcomePage;

public class MiniJavaCompiler {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WelcomePage().setVisible(true);
        });
    }
}