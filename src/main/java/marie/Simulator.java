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
            loader.load(objectCode);
            addMessage("Upload Completed");
            startMicroprocessor(objectCode);
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

        private void startMicroprocessor(int[][] objectCode) {
            try {
                microprocessor.start(objectCode[0][0]);
            } catch (ArrayIndexOutOfBoundsException e) {
                addMessage("microprocessor could not get origin address, check your program is not empty");
            }
        }

    public void run() {
        int i, limit = 100000;
        for (i = 0; !microprocessor.isStopped() && i < limit; i++)
            runNextInstruction();
        if (i == limit)
            addMessage("execution stopped: instruction run limit reached, check your program includes 'halt' command");
    }

    public void runNextInstruction() {
        try {
            microprocessor.runNextInstruction();
        } catch (Memory.InvalidAddress e) {
            addMessage("runtime error: instructions indirect address is invalid");
        }
    }

    public void reset() {
        memory.reset();
        compiler.reset();
        microprocessor.reset();
        clearConsoleMessage();
    }

    public void setInputDevice(String value) {
        if (isValidInput(value))
            microprocessor.setInput(Integer.parseInt(value, 16));
        else addMessage("invalid input device value");
    }

        private boolean isValidInput(String inputValue) {
            return inputValue.matches("[0-9a-fA-F]{1,4}");
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
