package marie;

import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionTest {

    Instruction instruction;

    @Test
    public void testInitialize() throws Exception {
        instruction = new Instruction("Halt");
        assertEquals("Instruction(Symbol: Halt)", instruction.toString());
        instruction = new Instruction("Load", "100");
        assertEquals("Instruction(Symbol: Load, Address: 100)", instruction.toString());
        instruction = new Instruction("Dec", "20");
        instruction.setLabel("X");
        assertEquals("Instruction(Label: X, Symbol: Dec, Address: 20)", instruction.toString());
        instruction = new Instruction("Load");
        instruction.setAddressLabel("X");
        assertEquals("Instruction(Symbol: Load, Address Label: X)", instruction.toString());
    }

}