package marie;

public class Parser {

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

}