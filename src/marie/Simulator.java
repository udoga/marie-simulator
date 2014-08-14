package marie;

import compiler.Compiler;

public class Simulator {

    private Memory memory = new Memory(0x1000);
    private compiler.Compiler compiler = new Compiler();
    private Loader loader = new Loader(memory);

    private String consoleMessage;

    public void uploadProgram(String sourceCode) {
        resetProperties();
        try {
            int[][] objectCode = compiler.compile(sourceCode);
            addCompilerMessage();
            loader.load(objectCode);
            addMessage("Upload Completed");
        } catch (Compiler.CompileError e) {
            addMessage(e.getMessage());
        }
    }

        private void resetProperties() {
            consoleMessage = null;
        }

        private void addCompilerMessage() {
            if (compiler.hasWarningMessage())
                addMessage(compiler.getWarningMessage());
            else addMessage("Compile Successful");
        }

        private void addMessage(String message) {
            if (consoleMessage == null)
                consoleMessage = message;
            else
                consoleMessage += "\n\n" + message;
        }

    public Memory getMemory() {
        return memory;
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public String getConsoleMessage() {
        return consoleMessage;
    }

}
