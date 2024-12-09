package minicompiler.actions;

import minicompiler.ui.EditorPage;

public class CodeRunner {

    private final String code;
    private final EditorPage editorPage;
    public CodeRunner(String code, EditorPage editorPage) {
        this.code = code;
        this.editorPage = editorPage;
    }

    public String execute() {
        if (code == null || code.trim().isEmpty()) {
            return "No code to run.";
        }

        StringBuilder resultText = new StringBuilder("Running the code...\n\n");

        // Perform Lexical Analysis
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer(code);
        String lexicalResult = lexicalAnalyzer.analyze();
        resultText.append("Lexical Analysis Result:\n").append(lexicalResult).append("\n");


        if (lexicalResult.contains("Lexical analysis failed")) {
            resultText.append("\nAborting execution due to lexical errors.\n");

            editorPage.getLexicalAnalysisButton().setEnabled(true);
            editorPage.getSyntaxAnalysisButton().setEnabled(false);
            editorPage.getSemanticAnalysisButton().setEnabled(false);
            return resultText.toString();

        } else {
            editorPage.getLexicalAnalysisButton().setEnabled(true);
            editorPage.getSyntaxAnalysisButton().setEnabled(true);
            editorPage.getSemanticAnalysisButton().setEnabled(true);
        }
        
        // Perform Syntax Analysis
        SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
        String syntaxResult = syntaxAnalyzer.performSyntaxAnalysis(code);
        resultText.append("Syntax Analysis Result:\n").append(syntaxResult).append("\n");

        if (syntaxResult.contains("Syntax error(s)") || syntaxResult.contains("Failed")) {
            resultText.append("\nAborting execution due to syntax errors.\n");

            editorPage.getLexicalAnalysisButton().setEnabled(true); // Enable Lexical Analysis button
            editorPage.getSyntaxAnalysisButton().setEnabled(true); // Enable Lexical Analysis button
            editorPage.getSemanticAnalysisButton().setEnabled(false); // Disable Semantic Analysis button
            return resultText.toString();
        } else {
            editorPage.getSemanticAnalysisButton().setEnabled(true);
        }

        // Perform Semantic Analysis
        SemanticAnalyzer semanticAnalyzer = new SemanticAnalyzer(code);
        String semanticResult = semanticAnalyzer.analyze();
        resultText.append("Semantic Analysis Result:\n").append(semanticResult).append("\n");

        if (semanticResult.contains("Error(s) detected")) {
            resultText.append("\nAborting execution due to semantic errors.\n");

            editorPage.getLexicalAnalysisButton().setEnabled(true);
            editorPage.getSyntaxAnalysisButton().setEnabled(true);
            editorPage.getSemanticAnalysisButton().setEnabled(true);
        } else {
            resultText.append("\nCode execution simulated. All analyses are complete.");
        }
        return resultText.toString();
    }
}