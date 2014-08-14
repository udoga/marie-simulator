package marie;

import org.junit.Test;
import java.util.HashMap;
import static org.junit.Assert.*;

public class InstructionTest {

    Instruction instruction;

    @Test
    public void testInitialize() throws Exception {
        instruction = new Instruction("Halt");
        assertEquals("Instruction(Symbol: halt)", instruction.toString());
        instruction = new Instruction("X:", "Halt");
        assertEquals("Instruction(Label: X, Symbol: halt)", instruction.toString());
        instruction = new Instruction("Load", "100");
        assertEquals("Instruction(Symbol: load, Address: 100)", instruction.toString());
        instruction = new Instruction("X:", "Load", "100");
        assertEquals("Instruction(Label: X, Symbol: load, Address: 100)", instruction.toString());
        instruction = new Instruction("Load", "X");
        assertEquals("Instruction(Symbol: load, Address Label: X)", instruction.toString());
        instruction = new Instruction("X:", "Load", "Y");
        assertEquals("Instruction(Label: X, Symbol: load, Address Label: Y)", instruction.toString());
        instruction = new Instruction("Dec", "10");
        assertEquals("Instruction(Symbol: dec, Data: 10)", instruction.toString());
        instruction = new Instruction("X:", "Hex", "1F");
        assertEquals("Instruction(Label: X, Symbol: hex, Data: 1F)", instruction.toString());
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenMissingTokens() throws Exception {
        instruction = new Instruction();
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenWrongSymbol() throws Exception {
        instruction = new Instruction("WrongSymbol", "10");
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenOnlyLabel() throws Exception {
        instruction = new Instruction("X:");
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenOnlyMriSymbol() throws Exception {
        instruction = new Instruction("Load");
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenAddressUsedInNonMri() throws Exception {
        instruction = new Instruction("Halt", "100");
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenDataIsInvalid() throws Exception {
        instruction = new Instruction("Dec", "X");
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenDataIsMoreThanMax() throws Exception {
        instruction = new Instruction("Hex", "10000");
    }

    @Test
    public void testHexCodeConversion() throws Exception {
        instruction = new Instruction("Halt");
        assertEquals(0x7000, instruction.convertToHexCode());
        instruction = new Instruction("Load", "100");
        assertEquals(0x1100, instruction.convertToHexCode());
        instruction = new Instruction("Add", "20");
        assertEquals(0x3020, instruction.convertToHexCode());
        instruction = new Instruction("Dec", "20");
        assertEquals(0x0014, instruction.convertToHexCode());
        instruction = new Instruction("Hex", "1A");
        assertEquals(0x001a, instruction.convertToHexCode());
    }

    @Test(expected = Instruction.InvalidConversion.class)
    public void testThrowsInvalidConversion_WhenAddressLabelWithMriSymbol() throws Exception {
        instruction = new Instruction("Load", "X");
        instruction.convertToHexCode();
    }

    @Test(expected = Instruction.InvalidConversion.class)
    public void testThrowsInvalidConversion_WhenOrgOrEndInstruction() throws Exception {
        instruction = new Instruction("Org", "100");
        instruction.convertToHexCode();
    }

    @Test
    public void testFindsOwnAddress_FromLabelAddressTable() throws Exception {
        HashMap<String, Integer> labelAddressTable = createLabelAddressTable();
        instruction = new Instruction("Load", "Y");
        instruction.findAddressFromTable(labelAddressTable);
        assertEquals("Instruction(Symbol: load, Address: 200, Address Label: Y)", instruction.toString());
        assertEquals(0x1200, instruction.convertToHexCode());
    }

    @Test(expected = Instruction.LabelAddressNotFound.class)
    public void testThrowsLabelAddressNotFound_WhenLabelNotInTheTable() throws Exception {
        HashMap<String, Integer> labelAddressTable = createLabelAddressTable();
        instruction = new Instruction("Load", "Z");
        instruction.findAddressFromTable(labelAddressTable);
    }

    private HashMap<String, Integer> createLabelAddressTable() {
        HashMap<String, Integer> labelAddressTable = new HashMap<String, Integer>();
        labelAddressTable.put("X", 0x100);
        labelAddressTable.put("Y", 0x200);
        return labelAddressTable;
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenAddressLabelWithOrg() throws Exception {
        instruction = new Instruction("ORG", "X");
    }

    @Test
    public void testSkipcondInstructionConversion() throws Exception {
        instruction = new Instruction("Skipcond", "400");
        assertEquals("Instruction(Symbol: skipcond, Condition: 400)", instruction.toString());
        assertEquals(0x8400, instruction.convertToHexCode());
    }

    @Test(expected = Instruction.InvalidInstruction.class)
    public void testThrowsInvalidInstruction_WhenAddressLabelWithSkipcond() throws Exception {
        instruction = new Instruction("Skipcond", "X");
    }

}