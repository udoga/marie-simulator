package compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class Compiler {

    private Parser parser = new Parser();
    private Instruction[] instructions;
    private String errorMessage;
    private String warningMessage;

    private HashMap<String, Integer> labelAddressTable = new HashMap<String, Integer>();

    public int[][] compile(String sourceCode) {
        reset();
        generateInstructions(sourceCode);
        processAddressAssignments();
        return generateObjectCode();
    }

    public void reset() {
        errorMessage = null;
        warningMessage = null;
        labelAddressTable.clear();
    }

    private void generateInstructions(String sourceCode) {
        String[][] tokens = parser.getTokens(sourceCode);
        ArrayList<Instruction> instructionList = createInstructionListAndCheckForErrors(tokens);
        throwCompileErrorIfErrorsExist();
        instructions = new Instruction[instructionList.size()];
        instructions = instructionList.toArray(instructions);
    }

        private ArrayList<Instruction> createInstructionListAndCheckForErrors(String[][] tokens) {
            ArrayList<Instruction> instructionList = new ArrayList<Instruction>();
            for (int i = 0; i < tokens.length; i++) {
                if (tokens[i].length != 0) {
                    try {
                        Instruction instruction = new Instruction(tokens[i]);
                        instruction.lineNo = i+1;
                        instructionList.add(instruction);
                    } catch (Instruction.InvalidInstruction e) {
                        addError(i+1, "invalid instruction error: " + e.getMessage());
                    }
                }
            } return instructionList;
        }

        private void addError(int lineNo, String message) {
            String newErrorMessage = "Line " + lineNo + ": "  + message;
            if (errorMessage == null)
                errorMessage = newErrorMessage;
            else
                errorMessage += "\n\n" + newErrorMessage;
        }

        private void throwCompileErrorIfErrorsExist() {
            if (errorMessage != null)
                throw new CompileError(errorMessage);
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
        checkSourceCodeLayout();
        ArrayList<int[]> objectCodeList = new ArrayList<int[]>();
        for (Instruction instruction: instructions) {
            if (instruction.memoryLocation != null)
                objectCodeList.add(new int[]{instruction.memoryLocation, instruction.convertToHexCode()});
        }
        int[][] objectCode = new int[objectCodeList.size()][2];
        return objectCodeList.toArray(objectCode);
    }

        private void findAndSetLabelsAddresses() {
            for (Instruction instruction: instructions) {
                try {
                    instruction.findAddressFromTable(labelAddressTable);
                } catch (Instruction.LabelAddressNotFound e) {
                    addError(instruction.lineNo, "instruction conversion error: " + e.getMessage());
                }
            }
            throwCompileErrorIfErrorsExist();
        }

        private void checkSourceCodeLayout() {
            boolean haltFound = false;
            for (Instruction instruction: instructions) {
                if (instruction.isHalt()) haltFound = true;
                if (!haltFound && instruction.isData())
                    addWarning(instruction.lineNo, "data instruction should be after the 'halt'");
            }
            if (!haltFound) {
                warningMessage = null;
                addWarning(null, "'halt' command not found");
            }
        }

            private void addWarning(Integer lineNo, String message) {
                String newMessage = "warning: " + message;
                if (lineNo != null) newMessage = "Line " + lineNo + ": " + newMessage;
                if (warningMessage == null)
                    warningMessage = newMessage;
                else
                    warningMessage += "\n\n" + newMessage;
            }

    public String[][] getLabelTableData(String[][] labelTableData) {
        labelTableData = clearLabelTableData(labelTableData);
        String[] labels = new String[labelAddressTable.size()];
        labels = labelAddressTable.keySet().toArray(labels);
        for (int i = 0; i < labels.length; i++) {
            labelTableData[i][0] = labels[i];
            labelTableData[i][1] = String.format("%03X", labelAddressTable.get(labels[i]));
        }
        return labelTableData;
    }

        private String[][] clearLabelTableData(String[][] labelTableData) {
            for (int i = 0; i < labelTableData.length; i++) {
                labelTableData[i][0] = null;
                labelTableData[i][1] = null;
            }
            return labelTableData;
        }

    public String getWarningMessage() {
        return warningMessage;
    }

    public boolean hasWarningMessage() {
        return warningMessage != null;
    }

    public class CompileError extends RuntimeException {
        public CompileError(String message) {
            super(message);
        }
    }

}
