package marie;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimulatorTest {

    Simulator simulator = new Simulator();

    @Test
    public void testUploadsProgramToMemory() {
        String sourceCode = "ORG 100\nLoad X\nHalt\nX: Dec 20\nEND";
        simulator.uploadProgram(sourceCode);

        assertEquals(0x1102, simulator.getMemory().read(0x100));
        assertEquals(0x7000, simulator.getMemory().read(0x101));
        assertEquals(0x0014, simulator.getMemory().read(0x102));
    }

}