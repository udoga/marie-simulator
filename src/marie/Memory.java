package marie;

import java.util.HashMap;

public class Memory {

    HashMap<Integer, Integer> cells = new HashMap<Integer, Integer>();

    public Memory(int capacity) {

    }

    public int read(int address) {
        Integer data = cells.get(address);
        if (data == null) return 0x0;
        return data;
    }

    public void write(int address, int data) {
        cells.put(address, data);
    }

    public void reset() {
        cells.clear();
    }

}
