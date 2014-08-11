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

}