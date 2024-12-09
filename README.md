# Mini Java Compiler

The **Mini Java Compiler** is a lightweight Java application designed to analyze, compile, and simulate the execution of basic Java code. It provides an integrated graphical user interface (GUI) for writing, analyzing, and understanding Java code.

## Features

- **Lexical Analysis**: Tokenizes the source code and identifies unknown tokens.
- **Syntax Analysis**: Validates code structure against Java grammar.
- **Semantic Analysis**: Checks for type mismatches and semantic errors.
- **GUI with Editor**:
  - Code editor with undo/redo, cut, copy, and paste functionality.
  - Syntax highlighting for better readability.
  - Analysis results displayed in a separate panel.
- **File Handling**: Open and edit `.java` files directly.
- **Zoom Controls**: Adjust the font size dynamically for accessibility.

## Installation

1. Ensure you have Java Development Kit (JDK) installed (version 8 or higher).
2. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/minijavacompiler.git
   cd minijavacompiler   
3. Compile the project:
   ```bash
   javac -d bin -sourcepath src src/minicompiler/main/MiniJavaCompiler.java
4. Run the application:
   ```bash
   java -cp bin minicompiler.main.MiniJavaCompiler

## How to Use

1. Launch the application and select Start on the welcome screen.
2. Use the editor to write or load Java code.
3. Perform analyses:
     * Lexical Analysis: Identifies tokens and highlights unknown tokens.
     * Syntax Analysis: Validates the code structure.
     * Semantic Analysis: Ensures type compatibility and detects semantic errors.
4. Click Run to execute all analyses sequentially.

## Folder Structure



## Development

### Prerequisites
  * Java JDK 8 or later
  * Java Swing (included in the JDK)
### Key Classes
  * `CodeRunner`: Executes the entire pipeline (lexical, syntax, and semantic analysis).
  * `LexicalAnalyzer`: Tokenizes and identifies lexical errors.
  * `SyntaxAnalyzer`: Validates the code's syntax.
  * `SemanticAnalyzer`: Checks type compatibility and validates logic.
  * `EditorPage`: The main user interface.
  * `FileLoader`: Handles file import operations.

## Contributions

Contributions are welcome! If you'd like to contribute:

1. Fork the repository.
2. Create a new branch for your feature/fix:
   ```bash
   git checkout -b feature-name
3. Commit your changes and open a pull request.