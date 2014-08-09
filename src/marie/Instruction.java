package marie;

import java.util.Arrays;

public class Instruction {

    private static final String mriSymbolRegex = "(jns)|(load)|(store)|(add)|(subt)|(jump)|(addi)|(jumpi)|(org)";
    private static final String nonMriSymbolRegex = "(input)|(output)|(halt)|(skipcond)|(clear)|(end)";
    private static final String dataSymbolRegex = "(hex)|(dec)";

    private static final String symbolRegex = mriSymbolRegex + "|" + nonMriSymbolRegex + "|" + dataSymbolRegex;
    private static final String addressRegex = "([0-9][0-9a-fA-F]{0,2})|(0+[0-9a-fA-F]{0,3})";
    private static final String addressLabelRegex = "[a-zA-Z]\\w*";
    private static final String labelRegex = addressLabelRegex + ":";
    private static final String hexDataRegex = "[a-fA-F0-9]+";
    private static final String decDataRegex = "\\d+";

    private String symbol;
    private String address;
    private String label;
    private String addressLabel;
    private String data;


    public Instruction(String... instructionTokens) {
        analyze(instructionTokens);
    }

    private void analyze(String[] instructionTokens) {
        if (instructionTokens.length == 0)
            throw new InvalidInstruction();
        instructionTokens = extractLabel(instructionTokens);
        findAndSetSymbol(instructionTokens);
        validateTokenCountBySymbolType(instructionTokens);
        findAndSetAddress(instructionTokens);
        findAndSetData(instructionTokens);
    }

        private String[] extractLabel(String[] instructionTokens) {
            if (instructionTokens[0].matches(labelRegex)) {
                label = instructionTokens[0].substring(0, instructionTokens[0].length()-1);
                return Arrays.copyOfRange(instructionTokens, 1, instructionTokens.length);
            } else return instructionTokens;
        }

        private void findAndSetSymbol(String[] instructionTokens) {
            if (instructionTokens.length == 0)
                throw new InvalidInstruction();
            if (!instructionTokens[0].toLowerCase().matches(symbolRegex))
                throw new InvalidInstruction();
            symbol = instructionTokens[0].toLowerCase();
        }

        private void validateTokenCountBySymbolType(String[] instructionTokens) {
            if (symbol.matches(mriSymbolRegex + "|" + dataSymbolRegex) && instructionTokens.length != 2)
                throw new InvalidInstruction();
            if (symbol.matches(nonMriSymbolRegex) && instructionTokens.length != 1)
                throw new InvalidInstruction();
        }

        private void findAndSetAddress(String[] instructionTokens) {
            if (symbol.matches(mriSymbolRegex)) {
                if (instructionTokens[1].matches(addressRegex))
                    address = instructionTokens[1];
                else if (instructionTokens[1].matches(addressLabelRegex))
                    addressLabel = instructionTokens[1];
                else throw new InvalidInstruction();
            }
        }

        private void findAndSetData(String[] instructionTokens) {
            if (symbol.matches(dataSymbolRegex)) {
                boolean hexDataValid = symbol.equals("hex") && instructionTokens[1].matches(hexDataRegex) &&
                        (Integer.parseInt(instructionTokens[1], 16) <= 0xFFFF);
                boolean decDataValid = symbol.equals("dec") && instructionTokens[1].matches(decDataRegex) &&
                        (Integer.parseInt(instructionTokens[1]) <= 0xFFFF);
                if (hexDataValid || decDataValid)
                    data = instructionTokens[1];
                else throw new InvalidInstruction();
            }
        }

    public String toString() {
        String labelInfo = (label == null)? "" : (", Label: " + label);
        String symbolInfo = (symbol == null)? "" : (", Symbol: " + symbol);
        String addressInfo = (address == null)? "" : (", Address: " + address);
        String addressLabelInfo = (addressLabel == null)? "" : (", Address Label: " + addressLabel);
        String dataInfo = (data == null)? "" : (", Data: " + data);

        String info = (labelInfo + symbolInfo + addressInfo + addressLabelInfo + dataInfo).substring(2);
        return "Instruction(" + info + ")";
    }

    public class InvalidInstruction extends RuntimeException {
    }

}
