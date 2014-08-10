package marie;


import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {

    private Parser parser = new Parser();
    private Instruction[] instructions;

    private HashMap<String, Integer> labelAddressTable = new HashMap<String, Integer>();

    public int[][] compile(String sourceCode) {
        generateInstructions(sourceCode);
        processAddressAssignments();
        return generateObjectCode();
    }

    private void generateInstructions(String sourceCode) {
        String tokens[][] = parser.getTokens(sourceCode);
        instructions = new Instruction[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            try { instructions[i] = new Instruction(tokens[i]); }
            catch (Instruction.InvalidInstruction e) {
                addError(i, "error message"); }
        }
    }

    private void addError(int lineNo, String message) {
        System.out.println("Error: " + lineNo + " - "  + message);
    }

    private void processAddressAssignments() {
        int locationCounter = 0;
        for (Instruction instruction: instructions) {
            if (instruction.isEnd()) break;
            if (instruction.isOrg()) {
                locationCounter = instruction.getAddress();
                continue;
            }
            if (instruction.hasLabel())
                labelAddressTable.put(instruction.getLabel(), locationCounter);
            instruction.memoryLocation = locationCounter;
            locationCounter++;
        }
    }

    private int[][] generateObjectCode() {
        findAndSetLabelsAddresses();
        ArrayList<int[]> objectCodeList = new ArrayList<int[]>();
        for (Instruction instruction: instructions) {
            if (instruction.memoryLocation != null)
                objectCodeList.add(new int[]{instruction.memoryLocation, instruction.convertToHexCode()});
        }
        int[][] objectCode = new int[objectCodeList.size()][2];
        return objectCodeList.toArray(objectCode);
    }

    private void findAndSetLabelsAddresses() {
        for (int i = 0; i < instructions.length; i++) {
            try {
                instructions[i].findAddressFromTable(labelAddressTable);
            } catch (Instruction.LabelAddressNotFound e) {
                addError(i, "error message");
            }
        }
    }

}
