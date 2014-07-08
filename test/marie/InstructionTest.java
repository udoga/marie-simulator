package marie;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class InstructionTest {

    Instruction instruction;

    @Before
    public void setUp() throws Exception {
        String[] terms = {"abc", "123"};
        instruction = new Instruction(terms);
    }

    @Test
    public void testConversionToString() {
        assertEquals("0-[abc, 123]", instruction.toString());
        instruction.setMemoryLocation(1);
        assertEquals("1-[abc, 123]", instruction.toString());
    }

    @Test
    public void testLabelInstructions() throws Exception {
        String[] terms = {"etiket:", "dec", "20"};
        instruction = new Instruction(terms);
        assertTrue(instruction.isLabelInstruction());
    }
}