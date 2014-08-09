package marie;

import java.util.Arrays;

public class Parser {

    private static final String mriSymbolRegex = "(jns)|(load)|(store)|(add)|(subt)|(jump)|(addi)|(jumpi)";
    private static final String nonmriSymbolRegex = "(input)|(output)|(halt)|(skipcond)|(clear)";

    private static final String symbolRegex = mriSymbolRegex + "|" + nonmriSymbolRegex;
    private static final String addressRegex = "[0-9][a-f0-9]{0,3}";
    private static final String labelRegex = "[a-zA-Z]\\w*:";
    private static final String addressLabelRegex = "[a-zA-Z]\\w*";

    public String[][] getTokens(String sourceCode) {
        String[] lines = sourceCode.split("(\\n(\\s)*(;.*)*)+");
        String[][] tokens = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = deleteComment(lines[i]);
            tokens[i] = lines[i].split("\\s+");
        }
        return tokens;
    }

        private String deleteComment(String line) {
            if (line.contains(";"))
                line = line.split(";")[0];
            return line;
        }

    public Instruction[] generateInstructions(String sourceCode) {
        String[][] tokens = getTokens(sourceCode);
        Instruction[] instructions = new Instruction[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            if (isValid(tokens[i]))
                instructions[i] = createInstructionFromTokenSentence(tokens[i]);
        }
        return instructions;
    }

        public boolean isValid(String[] tokenSentence) {
            if (tokenSentence.length > 0 && tokenSentence[0].matches(labelRegex))
                tokenSentence = Arrays.copyOfRange(tokenSentence, 1, tokenSentence.length);
            if (tokenSentence[0].toLowerCase().matches(nonmriSymbolRegex))
                System.out.println("huloo");

            return ((tokenSentence.length == 1 && tokenSentence[0].matches(nonmriSymbolRegex)) ||
                    (tokenSentence.length == 2 && tokenSentence[0].matches(mriSymbolRegex) && tokenSentence[1].matches(addressRegex)) ||
                    (tokenSentence.length == 2 && tokenSentence[0].matches(mriSymbolRegex) && tokenSentence[1].matches(addressLabelRegex)));
        }

        private Instruction createInstructionFromTokenSentence(String[] tokenSentence) {
            return new Instruction("");
        }

/*    private Instruction generateInstructionFromTokenSentence(String[] tokenSentence) {
        if (tokenSentence.length == 1 && tokens[i][0].matches(symbolRegex))
            instructions[i] = new Instruction(tokens[i][0]);
        else if (tokens[i].length == 2 && tokens[i][0].matches(labelRegex) && tokens[i][1].matches(symbolRegex)) {
            instructions[i] = new Instruction(tokens[i][0]);
            instructions[i].setLabel(tokens[i][0]);
        }
        else if (tokens[i].length == 2 && tokens[i][0].matches(symbolRegex) && tokens[i][1].matches(addressRegex))
            instructions[i] = new Instruction(tokens[i][0], tokens[i][1]);
        else if (tokens[i].length == 2 && tokens[i][0].matches(symbolRegex) && tokens[i][1].matches(addressLabelRegex)) {
            instructions[i] = new Instruction(tokens[i][0]);
            instructions[i].setAddressLabel(tokens[i][1]);
        }
        else if (tokens[i].length == 3 && tokens[i][0].matches(labelRegex) && tokens[i][1].matches(symbolRegex) && tokens[i][2].matches(addressRegex)) {
            instructions[i] = new Instruction(tokens[i][1], tokens[i][2]);
            instructions[i].setLabel(tokens[i][0]);
        }
        else if (tokens[i].length == 3 && tokens[i][0].matches(labelRegex) && tokens[i][1].matches(symbolRegex) && tokens[i][2].matches(addressLabelRegex)) {
            instructions[i] = new Instruction(tokens[i][1]);
            instructions[i].setLabel(tokens[i][0]);
            instructions[i].setAddressLabel(tokens[i][2]);
        }
        else {
            System.out.println("Gecersiz islem.");
        }
    } */

}