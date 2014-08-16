package marie;

import org.junit.Test;
import static org.junit.Assert.*;

public class LoaderTest {

    Memory memory = new Memory(0x1000);
    Loader loader = new Loader(memory);

    @Test
    public void testLoadsObjectCodeToMemory() {
        int[][] objectCode = {{0x100, 0x1102}, {0x101, 0x7000}, {0x102, 0x0014}};
        loader.load(objectCode);

        assertEquals(0x1102, memory.read(0x100));
        assertEquals(0x7000, memory.read(0x101));
        assertEquals(0x0014, memory.read(0x102));
    }

}