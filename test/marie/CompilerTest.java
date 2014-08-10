package marie;

import org.junit.Test;
import static org.junit.Assert.*;


public class CompilerTest {

    Compiler compiler = new Compiler();

    @Test
    public void testCompileSourceCode() {
        String sourceCode = "ORG 100\nLoad 102\nHalt\nDec 20\nEND";
        int[][] objectCode = {{0x100, 0x1102}, {0x101, 0x7000}, {0x102, 0x0014}};
        assertArrayEquals(objectCode, compiler.compile(sourceCode));
    }

    @Test
    public void testMultipleOrgStatements() throws Exception {
        String sourceCode = "ORG 100\nLoad 102\nHalt\nDec 20\nORG 200\nAdd 201\nHalt\nDec 30\nEND";
        int[][] objectCode = {{0x100, 0x1102}, {0x101, 0x7000}, {0x102, 0x0014},
                {0x200, 0x3201}, {0x201, 0x7000}, {0x202, 0x1e}};
        assertArrayEquals(objectCode, compiler.compile(sourceCode));
    }

    @Test
    public void testConvertLabelsToAddresses() throws Exception {
        String sourceCode = "ORG 100\nLoad X\nAdd Y\nSubt Z\nStore SONUC\n" +
                "Output\nHalt\nX: Dec 10\nY: Dec 20\nZ: Dec 5\nSONUC: Dec 0\nEND";
        int[][] objectCode = {{0x100, 0x1106}, {0x101, 0x3107}, {0x102, 0x4108}, {0x103, 0x2109}, {0x104, 0x6000},
                {0x105, 0x7000}, {0x106, 0x000a}, {0x107, 0x0014}, {0x108, 0x0005}, {0x109, 0x0000}};
        assertArrayEquals(objectCode, compiler.compile(sourceCode));
    }

}