package marie;

public class Loader {

    private Memory memory;

    public Loader(Memory memory) {
        this.memory = memory;
    }

    public void load(int[][] objectCode) {
        for (int[] objectCodeLine: objectCode)
            memory.write(objectCodeLine[0], objectCodeLine[1]);
    }

}
