package marie;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    Memory memory;

    @Before
    public void setUp() throws Exception {
        memory = new Memory(0x1000);
    }

    @Test
    public void data_ShouldBeEmptyBeforeWrite() {
        assertEquals(0x0, memory.read(0x100));
    }

    @Test
    public void readingData_ShouldBeSameAsWritten() {
        memory.write(0x100, 0xFFF);
        assertEquals(0XFFF, memory.read(0x100));
    }

    @Test
    public void writtenCell_ShouldBeEmptyAfterReset() {
        memory.write(0x100, 0xFFF);
        memory.reset();
        assertEquals(0x0, memory.read(0x100));
    }

}
