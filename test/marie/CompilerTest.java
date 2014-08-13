package marie;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void testThrowsCompileErrorAndShowsMessage() throws Exception {
        expectedEx.expect(Compiler.CompileError.class);
        expectedEx.expectMessage("Line 2: invalid instruction error: unknown symbol 'WrongSymbol'");

        String sourceCode = "ORG 100\nWrongSymbol 100\nHalt\nEND";
        compiler.compile(sourceCode);
    }

    @Test
    public void testCompileErrorMessage_ShowsMultipleErrors() throws Exception {
        String expectedErrorMessage =
                "Line 2: invalid instruction error: expected instruction symbol\n\n" +
                "Line 3: invalid instruction error: wrong token count, 'load' instruction" +
                    " should consist of one symbol and one address\n\n" +
                "Line 4: invalid instruction error: invalid address or address label '12z'\n\n" +
                "Line 5: invalid instruction error: invalid data\n\n" +
                "Line 6: invalid instruction error: wrong token count, 'halt' instruction" +
                    " should be only one symbol";
        expectedEx.expect(Compiler.CompileError.class);
        expectedEx.expectMessage(expectedErrorMessage);

        String sourceCode = "ORG 100\nX:\nLoad X Y\nStore 12z\nDec 65537\nHalt 20\nEND";
        compiler.compile(sourceCode);
    }

    @Test
    public void testThrowsCompileError_WhenLabelAddressNotFound() throws Exception {
        String expectedErrorMessage =
                "Line 2: instruction conversion error: undefined address label 'X'\n\n" +
                "Line 3: instruction conversion error: undefined address label 'Y'";
        expectedEx.expect(Compiler.CompileError.class);
        expectedEx.expectMessage(expectedErrorMessage);

        String sourceCode = "ORG 100\nLoad X\nAdd Y\nHalt\nEND";
        compiler.compile(sourceCode);
    }

    @Test
    public void testThrowsCompileError_WhenWrongOrgEndFormat() throws Exception {
        String expectedErrorMessage =
                "Line 1: invalid instruction error: org instruction address should be numeric\n\n" +
                "Line 3: invalid instruction error: wrong token count, 'end' instruction should be only one symbol";
        expectedEx.expect(Compiler.CompileError.class);
        expectedEx.expectMessage(expectedErrorMessage);

        String sourceCode = "ORG X\nX: Dec 10\nEND 100";
        compiler.compile(sourceCode);
    }

    @Test
    public void testErrorLineNumberShouldBeTrue_WhenSourceCodeIsMessy() throws Exception {
        String expectedErrorMessage =
                "Line 4: invalid instruction error: unknown symbol 'WrongSymbol'\n\n" +
                "Line 7: invalid instruction error: wrong token count, 'halt' instruction should be only one symbol";
        expectedEx.expect(Compiler.CompileError.class);
        expectedEx.expectMessage(expectedErrorMessage);

        String sourceCode = "\n\n ORG 100\nWrongSymbol\n  \n\nHalt 100\n";
        compiler.compile(sourceCode);
    }

}