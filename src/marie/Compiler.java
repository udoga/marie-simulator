package marie;

import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {

    Instruction[] instructions;
    private HashMap<String, Integer> symbolAddressTable = new HashMap<String, Integer>();
    int[][] objectCode;

    public int[][] compile(String sourceCode) {
        generateInstructions(sourceCode);
        processAddressAssignments();
        generateObjectCodeFromInstructions();
        return objectCode;
    }

    private void generateInstructions(String sourceCode) {
        sourceCode = sourceCode.toLowerCase();
        String[] lines = sourceCode.split("(\\n(\\s)*(;.*)*)+");
        instructions = new Instruction[lines.length];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = deleteComment(lines[i]);
            String[] lineTerms = lines[i].split("\\s+");
            instructions[i] = new Instruction(lineTerms);
        }
    }

        private String deleteComment(String line) {
            if (line.contains(";"))
                line = line.split(";")[0];
            return line;
        }

    private void processAddressAssignments() {
        int locationCounter = 0;
        ArrayList<Instruction> nonPseudoInstructions = new ArrayList<Instruction>();
        for (Instruction instruction: instructions) {
            if (instruction.isEndInstruction()) break;
            if (instruction.isOrgInstruction()) {
                locationCounter = instruction.getAddress();
                continue;
            }
            if (instruction.isLabelInstruction()) {
                symbolAddressTable.put(instruction.getLabel(), locationCounter);
                instruction.removeLabelPart();
            }
            instruction.setMemoryLocation(locationCounter);
            nonPseudoInstructions.add(instruction);
            locationCounter++;
        }
        instructions = new Instruction[nonPseudoInstructions.size()];
        instructions = nonPseudoInstructions.toArray(instructions);
    }

    private void generateObjectCodeFromInstructions() {
        objectCode = new int[instructions.length][2];
        for (int i = 0; i < instructions.length; i++) {
            instructions[i] = replaceAddressLabelWithAddressIfItContains(instructions[i]);
            objectCode[i] = new int[]{instructions[i].getMemoryLocation(), instructions[i].toHexCode()};
        }
    }

        private Instruction replaceAddressLabelWithAddressIfItContains(Instruction instruction) {
            if (instruction.containsAddressLabel()) {
                Integer address = symbolAddressTable.get(instruction.getAddressLabel());
                if (address == null) throw new LabelNotFound();
                else instruction.setAddress(address);
            }
            return instruction;
        }

    public class LabelNotFound extends RuntimeException {
    }

}