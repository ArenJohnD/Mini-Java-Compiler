package actions;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LexicalAnalyzer {

    private String code;

    public LexicalAnalyzer(String code) {
        this.code = code;
    }

    public String analyze() {
        if (code.isEmpty()) {
            return "No code to analyze.";
        }
    
        String regex = "^\\s*" +
                "(\\b(?:byte|short|int|long|float|double|boolean|char|String)\\b)?\\s*" +
                "([a-zA-Z_][a-zA-Z0-9_]*)?\\s*" +
                "(=)?\\s*" +
                "((\"[^\"]*\"|'[^']*'|\\d+(\\.\\d+)?|true|false|'.'|\\bnull\\b))?\\s*" +
                "(;)?\\s*$";
    
        StringBuilder result = new StringBuilder();
        String[] lines = code.split("\\n");
        boolean error = false;
    
        for (String line : lines) {
            line = line.trim();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(line);
    
            if (matcher.matches()) {
                if (matcher.group(1) != null) {
                    result.append("Data Type: ").append(matcher.group(1)).append("\n");
                }
                if (matcher.group(2) != null) {
                    result.append("Identifier: ").append(matcher.group(2)).append("\n");
                }
                if (matcher.group(3) != null) {
                    result.append("Assignment Operator: ").append(matcher.group(3)).append("\n");
                }
                if (matcher.group(4) != null && !matcher.group(4).isEmpty()) {
                    result.append("Value: ").append(matcher.group(4)).append("\n");
                }
                if (matcher.group(6) != null && !matcher.group(6).isEmpty()) {
                    result.append("Delimiter: ").append(matcher.group(6)).append("\n");
                }
                result.append("\n");
            } else {
                result.append("Unknown Token: ").append(line).append("\n");
                error = true;
            }
        }
    
        if (error) {
            result.append("\nUnknown token(s) detected. Lexical analysis failed.\n");
        } else {
            result.append("Lexical Analysis completed successfully.\n");
        }
    
        return result.toString();
    }
}    