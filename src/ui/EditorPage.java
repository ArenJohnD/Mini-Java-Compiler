package ui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.undo.UndoManager;

import actions.*;

public class EditorPage extends JFrame {
    private JTextArea codeTextArea;
    private final JTextArea resultTextArea = new JTextArea();
    private final JButton lexicalAnalysisButton = createButton("Lexical Analysis");
    private final JButton syntaxAnalysisButton = createButton("Syntax Analysis");
    private final JButton semanticAnalysisButton = createButton("Semantic Analysis");
    private final JButton runButton = createButton("Run");
    private UndoManager undoManager = new UndoManager();

    private FileLoader openFileHandler;

    public EditorPage() {
        setTitle("Mini Java Compiler");
        setSize(1000, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(createMenuBar());
        add(createHeaderLabel(), BorderLayout.NORTH);
        add(createSplitPane(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        openFileHandler = new FileLoader(this);

        lexicalAnalysisButton.setEnabled(false);
        syntaxAnalysisButton.setEnabled(false);
        semanticAnalysisButton.setEnabled(false);

        addContextMenuToCodeEditor();
        addZoomFunctionality();

    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(createMenuItem("Open File...", e -> openFile()));
        fileMenu.add(createMenuItem("New Window", e -> openNewWindow()));
        fileMenu.addSeparator();
        fileMenu.add(createMenuItem("Exit", e -> System.exit(0)));

        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        
        editMenu.add(createMenuItem("Undo", e -> {
            if (undoManager.canUndo()) {
                undoManager.undo();
            }
        }, KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
        
        editMenu.add(createMenuItem("Redo", e -> {
            if (undoManager.canRedo()) {
                undoManager.redo();
            }
        }, KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK));
        
        editMenu.addSeparator();
        
        editMenu.add(createMenuItem("Cut", e -> codeTextArea.cut(), KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(createMenuItem("Copy", e -> codeTextArea.copy(), KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(createMenuItem("Paste", e -> codeTextArea.paste(), KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
        editMenu.add(createMenuItem("Delete", e -> codeTextArea.replaceSelection(""), KeyEvent.VK_DELETE, 0));
        
        editMenu.addSeparator();
        
        editMenu.add(createMenuItem("Select All", e -> codeTextArea.selectAll(), KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
        
        JMenu viewMenu = new JMenu("View");
        menuBar.add(viewMenu);
        
        JMenu zoomMenu = new JMenu("Zoom");
        viewMenu.add(zoomMenu);
        
        zoomMenu.add(createMenuItem("Zoom In", e -> zoomIn(), KeyEvent.VK_EQUALS, KeyEvent.CTRL_DOWN_MASK));
        zoomMenu.add(createMenuItem("Zoom Out", e -> zoomOut(), KeyEvent.VK_MINUS, KeyEvent.CTRL_DOWN_MASK));
        zoomMenu.addSeparator();
        zoomMenu.add(createMenuItem("Restore Default Zoom", e -> restoreDefaultZoom(), KeyEvent.VK_0, KeyEvent.CTRL_DOWN_MASK));
        

        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        helpMenu.add(createMenuItem("About", e -> showAboutDialog()));

        return menuBar;
    }
    

    private JLabel createHeaderLabel() {
        JLabel headerLabel = new JLabel("Mini Java Compiler", JLabel.CENTER);
        headerLabel.setFont(new Font("Source Code Pro", Font.BOLD, 28));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return headerLabel;
    }

    private JSplitPane createSplitPane() {
        codeTextArea = new JTextArea();
        codeTextArea.setFont(new Font("Consolas", Font.PLAIN, 18));
    
        // Create a JScrollPane for the code editor
        JScrollPane codeScrollPane = new JScrollPane(codeTextArea);
        codeScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Code Editor"));
        codeTextArea.getDocument().addUndoableEditListener(e -> undoManager.addEdit(e.getEdit()));
    
        // Create a layered panel to overlay the "Run" button
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(null);
    
        // Add the scroll pane to the layered pane
        codeScrollPane.setBounds(0, 0, 800, 400); // Adjust these dimensions to match your layout
        layeredPane.add(codeScrollPane, Integer.valueOf(0)); // Background layer
    
        // Configure the "Run" button and add it to the layered pane
        runButton.setBounds(700, 350, 100, 40); // Adjust button size and position
        layeredPane.add(runButton, Integer.valueOf(1)); // Foreground layer
    
        // Add a listener to resize components dynamically
        layeredPane.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                codeScrollPane.setSize(layeredPane.getWidth(), layeredPane.getHeight());
                runButton.setLocation(layeredPane.getWidth() - runButton.getWidth() - 10,
                                      layeredPane.getHeight() - runButton.getHeight() - 10);
            }
            
        });

        
        // Create a JScrollPane for the result text area
        resultTextArea.setFont(new Font("Courier New", Font.PLAIN, 16));
        resultTextArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Analysis Results"));
    
        // Combine the layered pane and result pane in a split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, layeredPane, resultScrollPane);
        splitPane.setDividerLocation(0.7); // 70% of the space for the code editor
        splitPane.setResizeWeight(0.7);    // Prioritize resizing the code editor
    
        return splitPane;
    }

    public JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
        // Adding action listeners to buttons
        lexicalAnalysisButton.addActionListener(e -> performLexicalAnalysis());
        syntaxAnalysisButton.addActionListener(e -> performSyntaxAnalysis());
        semanticAnalysisButton.addActionListener(e -> performSemanticAnalysis());

            runButton.addActionListener(e -> {
            String code = codeTextArea.getText().trim();
            CodeRunner codeRunner = new CodeRunner(code, this); // Pass EditorPage reference
            String result = codeRunner.execute(); // Run the analysis
            resultTextArea.setText(result); // Display the result in the result pane
        });

        buttonPanel.add(lexicalAnalysisButton);
        buttonPanel.add(syntaxAnalysisButton);
        buttonPanel.add(semanticAnalysisButton);
    
        return buttonPanel;
    }
    
    public JButton getLexicalAnalysisButton() {
        return lexicalAnalysisButton;
    }

    public JButton getSyntaxAnalysisButton() {
        return syntaxAnalysisButton;
    }

    public JButton getSemanticAnalysisButton() {
        return semanticAnalysisButton;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Source Code Pro", Font.PLAIN, 16));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener) {
        JMenuItem menuItem = new JMenuItem(text);
        menuItem.addActionListener(actionListener);
        return menuItem;
    }

    private JMenuItem createMenuItem(String text, ActionListener actionListener, int keyCode, int modifiers) {
        JMenuItem menuItem = new JMenuItem(text);
        KeyStroke shortcut = KeyStroke.getKeyStroke(keyCode, modifiers);
        menuItem.setAccelerator(shortcut); // This ensures the shortcut is displayed in the menu
        menuItem.addActionListener(actionListener);
        return menuItem;
    }
    
    private void addContextMenuToCodeEditor() {
        JPopupMenu contextMenu = new JPopupMenu();
    
        // Create menu items
        JMenuItem undoItem = new JMenuItem("Undo");
        JMenuItem redoItem = new JMenuItem("Redo");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem selectAllItem = new JMenuItem("Select All");
    
        // Attach actions
        undoItem.addActionListener(e -> {
            if (undoManager.canUndo()) undoManager.undo();
        });
        redoItem.addActionListener(e -> {
            if (undoManager.canRedo()) undoManager.redo();
        });
        cutItem.addActionListener(e -> codeTextArea.cut());
        copyItem.addActionListener(e -> codeTextArea.copy());
        pasteItem.addActionListener(e -> codeTextArea.paste());
        deleteItem.addActionListener(e -> codeTextArea.replaceSelection(""));
        selectAllItem.addActionListener(e -> codeTextArea.selectAll());
    
        // Add menu items to context menu
        contextMenu.add(undoItem);
        contextMenu.add(redoItem);
        contextMenu.addSeparator();
        contextMenu.add(cutItem);
        contextMenu.add(copyItem);
        contextMenu.add(pasteItem);
        contextMenu.add(deleteItem);
        contextMenu.addSeparator();
        contextMenu.add(selectAllItem);
    
        // Add mouse listener to codeTextArea for the context menu
        codeTextArea.setComponentPopupMenu(contextMenu);
    }
    

    private void openFile() {
        openFileHandler.openFile();
        resetAnalysisButtons();

    }

    protected void triggerOpenFile() {
        openFile();
    }

    private void openNewWindow() {
        SwingUtilities.invokeLater(() -> {
            EditorPage newMainPage = new EditorPage();
            newMainPage.setVisible(true);
        });
    }

    private void zoomIn() {
        Font currentFont = codeTextArea.getFont();
        int newSize = currentFont.getSize() + 2;
        codeTextArea.setFont(currentFont.deriveFont((float) newSize));
    }

    private void zoomOut() {
        Font currentFont = codeTextArea.getFont();
        int newSize = currentFont.getSize() - 2;
        if (newSize > 4) {  // Ensure the font size doesn't get too small
            codeTextArea.setFont(currentFont.deriveFont((float) newSize));
        }
    }

    private void restoreDefaultZoom() {
        codeTextArea.setFont(new Font("Source Code Pro", Font.PLAIN, 16)); //
    }

    private void addZoomFunctionality() {
        codeTextArea.addMouseWheelListener(e -> {
            if (e.isControlDown()) {
                Font currentFont = codeTextArea.getFont();
                int newSize = currentFont.getSize();
    
                if (e.getWheelRotation() < 0) { // Zoom in
                    newSize += 2;
                } else if (e.getWheelRotation() > 0) { // Zoom out
                    newSize -= 2;
                    if (newSize < 8) newSize = 8; // Minimum font size
                }
    
                codeTextArea.setFont(currentFont.deriveFont((float) newSize));
            }
        });
    }

    private void resetAnalysisButtons() {
        lexicalAnalysisButton.setEnabled(false);
        syntaxAnalysisButton.setEnabled(false);
        semanticAnalysisButton.setEnabled(false);
    }

    private void showAboutDialog() {
        // Create a new dialog
        JDialog aboutDialog = new JDialog(this, "About Mini Java Compiler", true);
        aboutDialog.setSize(400, 340);
        aboutDialog.setLocationRelativeTo(this); // Center the dialog relative to the parent frame

        // Create a content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add content to the panel
        JLabel titleLabel = new JLabel("Mini Java Compiler", JLabel.CENTER);
        titleLabel.setFont(new Font("Source Code Pro", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel descriptionLabel = new JLabel("<html>This Mini Java Compiler allows you to write, analyze, and execute simple Java code.<br><br>"
        + "Features include lexical, syntax, and semantic analysis.<br><br>"
        + "Built using Java Swing for GUI and supporting basic compiler functionalities.</html>",
        JLabel.CENTER);
        descriptionLabel.setFont(new Font("Source Code Pro", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Add a OK button
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setPreferredSize(new Dimension(100, 50)); // Same size as the Run button
        okButton.setFont(new Font("Source Code Pro", Font.PLAIN, 16)); // Same font as the Run button
        okButton.setContentAreaFilled(false); // Same transparency as the Run button
        okButton.setOpaque(true);
        okButton.setBackground(new Color(70, 130, 180)); // Same background color as the Run button
        okButton.setForeground(Color.WHITE); // White text like the Run button
        okButton.setFocusPainted(false); // Remove focus outline

        okButton.addActionListener(e -> aboutDialog.dispose());    

        // Add components to the content panel
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        contentPanel.add(descriptionLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing
        contentPanel.add(okButton);

        // Add content panel to the dialog
        aboutDialog.add(contentPanel);

        // Display the dialog
        aboutDialog.setVisible(true);
    }

    private void performLexicalAnalysis() {
        String code = codeTextArea.getText().trim();
        if (code.isEmpty()) {
            resultTextArea.setText("No code to analyze.");
            return;
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(code);
        String lexicalResult = lexicalAnalyzer.analyze();
        resultTextArea.setText(lexicalResult);

        if (lexicalResult.contains("Lexical analysis failed")) {
            syntaxAnalysisButton.setEnabled(false);
            semanticAnalysisButton.setEnabled(false);
        } else {
            syntaxAnalysisButton.setEnabled(true);
        }
    }

    private void performSyntaxAnalysis() {
        String code = codeTextArea.getText().trim();
        if (code.isEmpty()) {
            resultTextArea.setText("No code to analyze.");
            return;
        }

        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
        String syntaxResult = syntaxAnalyzer.performSyntaxAnalysis(code);
        resultTextArea.setText(syntaxResult);

        if (syntaxResult.contains("Syntax analysis failed")) {
            semanticAnalysisButton.setEnabled(false);
        } else {
            semanticAnalysisButton.setEnabled(true);
        }
    }

    private void performSemanticAnalysis() {
        String code = codeTextArea.getText().trim();

        if (code.isEmpty()) {
            resultTextArea.setText("No code to analyze.");
            return;
        }

        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(code);
        String semanticResult = semanticAnalyzer.analyze();
        resultTextArea.setText(semanticResult);
    }

    public JTextArea getCodeTextArea() {
        return codeTextArea;
    }
        
    public JTextArea getResultTextArea() {
        return resultTextArea;
    }
}