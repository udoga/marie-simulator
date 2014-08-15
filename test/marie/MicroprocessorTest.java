package marie;

import org.junit.Test;
import static org.junit.Assert.*;

public class MicroprocessorTest {

    Memory memory = new Memory(0x1000);
    Microprocessor microprocessor = new Microprocessor(memory);

    @Test
    public void testRunsBasicInstructions() throws Exception {
        uploadProgramOne(memory);
        microprocessor.start(0x100);
                                    // AC      MAR     MBR     IR      PC      OutREG  InREG
        runNextAndExpectRegisterValues(0x000A, 0x0106, 0x000A, 0x1106, 0x0101, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x001E, 0x0107, 0x0014, 0x3107, 0x0102, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0019, 0x0108, 0x0005, 0x4108, 0x0103, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0019, 0x0109, 0x0019, 0x2109, 0x0104, 0x0000, 0x0000);
        assertEquals(0x0019, memory.read(0x109));
        runNextAndExpectRegisterValues(0x0019, 0x0000, 0x0000, 0x6000, 0x0105, 0x0019, 0x0000);
        runNextAndExpectRegisterValues(0x0019, 0x0000, 0x0000, 0x7000, 0x0106, 0x0019, 0x0000);
    }

    @Test
    public void testRunProgram() throws Exception {
        uploadProgramOne(memory);
        microprocessor.start(0x100);
        microprocessor.run();
        assertEquals(0x0019, memory.read(0x109));
    }

    @Test
    public void testRunsJumpInstructions() throws Exception {
        uploadProgramTwo(memory);
        microprocessor.start(0x100);
                                    // AC      MAR     MBR     IR      PC      OutREG  InREG
        runNextAndExpectRegisterValues(0x0000, 0x0000, 0x0000, 0x5000, 0x0101, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0000, 0x0400, 0x0000, 0x8400, 0x0103, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0109, 0x0108, 0x0108, 0x0108, 0x0109, 0x0000, 0x0000);
        assertEquals(0x0104, memory.read(0x108));
        runNextAndExpectRegisterValues(0x010A, 0x010C, 0x0001, 0xB10B, 0x010A, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x010A, 0x0108, 0x0104, 0xC108, 0x0104, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0000, 0x0000, 0x0000, 0xA000, 0x0105, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0000, 0x0107, 0x7000, 0x9107, 0x0107, 0x0000, 0x0000);
        runNextAndExpectRegisterValues(0x0000, 0x0000, 0x0000, 0x7000, 0x0108, 0x0000, 0x0000);
    }

    private void runNextAndExpectRegisterValues(int... expectedRegisterValues) {
        microprocessor.runNextInstruction();
        assertArrayEquals(expectedRegisterValues, microprocessor.getRegisterValues());
    }

    private void uploadProgramOne(Memory memory) {
    /*  ORG 100 / Load X / Add Y / Subt Z / Store RESULT / Output /
        Halt / X, Dec 10 / Y, Dec 20 / Z, Dec 5 / RESULT, Dec 0 / END */
        int originAddress = 0x100;
        int[] hexCodes = {0x1106, 0x3107, 0x4108, 0x2109, 0x6000, 0x7000, 0x000A, 0x0014, 0x0005, 0x0000};
        for (int i = 0; i < hexCodes.length; i++)
            memory.write(originAddress+i, hexCodes[i]);
    }

    private void uploadProgramTwo(Memory memory) {
    /*  ORG 100 / Input / Skipcond 400 / Load X / Jns Inc / Clear / Jump H
        Load X / H, Halt / Inc, Dec 0 / AddI X / JumpI Inc / X, Hex 10C / Dec 1 */
        int originAddress = 0x100;
        int[] hexCodes = {0x5000, 0x8400, 0x110B, 0x0108, 0xA000, 0x9107,
                0x110B, 0x7000, 0x0000, 0xB10B, 0xC108, 0x010C, 0x0001};
        for (int i = 0; i < hexCodes.length; i++)
            memory.write(originAddress+i, hexCodes[i]);
    }

}