package marie;

import org.junit.Test;

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
        instruction = new Instruction("X:", "Dec", "10");
        assertEquals("Instruction(Label: X, Symbol: dec, Data: 10)", instruction.toString());
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

}