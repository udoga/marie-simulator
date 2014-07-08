package marie;

import java.util.Arrays;

public class Instruction {

    private static final String[] symbols =
        {"jns", "load", "store", "add", "subt", "input", "output",
         "halt","skipcond", "jump", "clear", "addi", "jumpi"};

    String[] terms;
    private int memoryLocation;

    public Instruction(String[] terms) {
        this.terms = terms;
    }

    public String toString() {
        return memoryLocation + "-" + Arrays.toString(terms);
    }

    public int getMemoryLocation() {
        return memoryLocation;
    }

    public void setMemoryLocation(int memoryLocation) {
        this.memoryLocation = memoryLocation;
    }

    public boolean isEndInstruction() {
        return terms[0].equals("end");
    }

    public boolean isOrgInstruction() {
        return terms[0].equals("org");
    }

    public int getAddress() {
        return Integer.parseInt(terms[1], 16);
    }

    public boolean isLabelInstruction() {
        return terms[0].matches("[a-z]\\w*:");
    }

    public String getLabel() {
        return terms[0].split(":")[0];
    }

    public void removeLabelPart() {
        terms = Arrays.copyOfRange(terms, 1, terms.length);
    }

    public boolean containsAddressLabel() {
        return (terms.length > 1) && terms[1].matches("[a-z]\\w*");
    }

    public String getAddressLabel() {
        return terms[1];
    }

    public void setAddress(Integer address) {
        terms[1] = String.format("%03x", address);
    }

    public int toHexCode() {
        if (terms[0].equals("dec"))
            return Integer.parseInt(terms[1], 10);
        else if (terms[0].equals("hex"))
            return Integer.parseInt(terms[1], 16);
        else {
            String address = (terms.length > 1) ? terms[1] : "000";
            return Integer.parseInt(getOpcode() + address, 16);
        }
    }

    private String getOpcode() {
        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equals(terms[0]))
                return Integer.toHexString(i);
        }
        throw new SymbolNotFound();
    }

    private class SymbolNotFound extends RuntimeException {
    }
}