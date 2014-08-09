package marie;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class ParserTest {

    Parser parser = new Parser();

    @Test
    public void testSeparateIntoTokens() throws Exception {
        String sourceCode = "ORG 100\n Load X\n   Add  Y   \n\n Store Z  ;Result \nHalt\n  \n;Some comment\n " +
                "X: Dec  10\n  Y:  Dec 20\n\n Z:  Dec  0\n \nEND\n;Some comment ";
        String[][] expectedTokens = {{"ORG", "100"}, {"Load", "X"}, {"Add", "Y"}, {"Store", "Z"}, {"Halt"},
                {"X:", "Dec", "10"}, {"Y:", "Dec", "20"}, {"Z:", "Dec", "0"}, {"END"}};
        assertArrayEquals(expectedTokens, parser.getTokens(sourceCode));
    }

    /*@Test
    public void testCreateInstructionFromTokenSentence() throws Exception {
        String[] tokenSentence = {"Halt"};
        Instruction instruction = parser.createInstructionFromTokenSentence(tokenSentence);
        assertEquals("Instruction(Symbol: Halt)", instruction.toString());

        tokenSentence = new String[]{"X:", "Dec", "20"};
        instruction = parser.createInstructionFromTokenSentence(tokenSentence);
        assertEquals("Instruction(Label: X, Symbol: Dec, Address: 20)", instruction.toString());
    }*/

    @Test
    public void testTokenSentenceValidation() throws Exception {
        String[] tokenSentence;
        tokenSentence = new String[]{"Halt"};
        assertTrue(parser.isValid(tokenSentence));
        tokenSentence = new String[]{"Load", "100"};
        assertTrue(parser.isValid(tokenSentence));
        tokenSentence = new String[]{"Load", "X"};
        assertTrue(parser.isValid(tokenSentence));
    }

    /*    @Test
    public void testGenerateInstructions() throws Exception {
        String sourceCode = "Load X\nHalt\nX: Dec 20";
        Instruction[] instructions = parser.generateInstructions(sourceCode);
        assertEquals("Instruction(Symbol: Load, Address Label: X)", instructions[0].toString());
        assertEquals("Instruction(Symbol: Halt)", instructions[1].toString());
        assertEquals("Instruction(Label: X, Symbol: Dec, Address: 20)", instructions[2].toString());
    } */

}