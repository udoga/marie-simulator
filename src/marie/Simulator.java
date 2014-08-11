package marie;

public class Simulator {

    private Memory memory = new Memory(0x1000);
    private marie.Compiler compiler = new Compiler();
    private Loader loader = new Loader(memory);

    public void uploadProgram(String sourceCode) {
        int[][] objectCode = compiler.compile(sourceCode);
        loader.load(objectCode);
    }

    public Memory getMemory() {
        return memory;
    }

    public Compiler getCompiler() {
        return compiler;
    }

}
