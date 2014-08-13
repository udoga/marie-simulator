package marie;

import java.util.Arrays;

public class Parser {

    public String[][] getTokens(String sourceCode) {
        String[] lines = getLines(sourceCode);
        String[][] tokens = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            lines[i] = deleteComment(lines[i]);
            tokens[i] = lines[i].split("\\s+");
        }
        return tokens;
    }

        private String[] getLines(String sourceCode) {
            String[] lines = sourceCode.split("(\\n(\\s)*(;.*)*)+");
            if (lines[0].isEmpty())
                lines = Arrays.copyOfRange(lines, 1, lines.length);
            return lines;
        }

        private String deleteComment(String line) {
            if (line.contains(";"))
                line = line.split(";")[0];
            return line;
        }

}