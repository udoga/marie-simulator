package marie;

import compiler.Compiler;

public class Simulator {

    private Memory memory = new Memory(0x1000);
    private Compiler compiler = new Compiler();
    private Microprocessor microprocessor = new Microprocessor(memory);
    private Loader loader = new Loader(memory);

    private String consoleMessage;

    public void uploadProgram(String sourceCode) {
        clearConsoleMessage();
        try {
            int[][] objectCode = compiler.compile(sourceCode);
            addCompilerMessage();
            microprocessor.start(objectCode[0][0]);
            loader.load(objectCode);
            addMessage("Upload Completed");
        } catch (Compiler.CompileError e) {
            addMessage(e.getMessage());
        }
    }

        private void clearConsoleMessage() {
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

    public void run() {
        microprocessor.run();
    }

    public void runNextInstruction() {
        microprocessor.runNextInstruction();
    }

    public void reset() {
        memory.reset();
        compiler.resetProperties();
        microprocessor.reset();
        clearConsoleMessage();
    }

    public Memory getMemory() {
        return memory;
    }

    public Compiler getCompiler() {
        return compiler;
    }

    public Microprocessor getMicroprocessor() {
        return microprocessor;
    }


    public String getConsoleMessage() {
        return consoleMessage;
    }

}
