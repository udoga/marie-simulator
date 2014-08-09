package marie;

import java.util.HashMap;


public class Memory {

    private int capacity;
    private HashMap<Integer, Integer> cells = new HashMap<Integer, Integer>();

    public Memory(int capacity) {
        this.capacity = capacity;
    }

    public int read(int address) {
        if (!isValidAddress(address))
            throw new InvalidAddress();
        Integer data = cells.get(address);
        if (data == null) return 0x0;
        return data;
    }

    public void write(int address, int data) {
        if (!isValidAddress(address))
            throw new InvalidAddress();
        cells.put(address, data);
    }

    public void reset() {
        cells.clear();
    }

    private boolean isValidAddress(int address) {
        return (address >= 0 && address < capacity);
    }

    public class InvalidAddress extends RuntimeException {
    }

}