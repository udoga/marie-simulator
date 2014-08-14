package compiler;

import java.util.Arrays;

public class Parser {

    public String[][] getTokens(String sourceCode) {
        String[] lines = sourceCode.split("\\n");
        String[][] tokens = new String[lines.length][];
        for (int i = 0; i < lines.length; i++)
            tokens[i] = getLineTokens(lines[i]);
        return tokens;
    }

    private String[] getLineTokens(String line) {
        line = deleteComment(line);
        if (line.matches("\\s*")) return new String[]{};
        String[] lineTokens = line.split("\\s+");
        if (lineTokens[0].isEmpty())
            lineTokens = Arrays.copyOfRange(lineTokens, 1, lineTokens.length);
        return lineTokens;
    }

    private String deleteComment(String line) {
        if (line.contains(";"))
            line = line.split(";")[0];
        return line;
    }

}