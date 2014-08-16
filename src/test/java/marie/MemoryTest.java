package marie;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MemoryTest {

    Memory memory;

    @Before
    public void setUp() {
        memory = new Memory(0x1000);
    }

    @Test
    public void dataIsEmptyBeforeWrite() {
        assertEquals(0x0, memory.read(0x100));
    }

    @Test
    public void readingDataIsTheSameAsWritten() {
        memory.write(0x100, 0xFFF);
        assertEquals(0XFFF, memory.read(0x100));
    }

    @Test
    public void writtenCellIsEmptyAfterReset() {
        memory.write(0x100, 0xFFF);
        memory.reset();
        assertEquals(0x0, memory.read(0x100));
    }

    @Test(expected = Memory.InvalidAddress.class)
    public void ThrowInvalidAddressWhenInvalidAddressEntered() {
        memory.read(0x1001);
    }

}