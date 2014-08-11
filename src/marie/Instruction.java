package marie;

import java.util.Arrays;
import java.util.HashMap;

public class Instruction {

    private static final String mriSymbolRegex = "(jns)|(load)|(store)|(add)|(subt)|(jump)|(addi)|(jumpi)|(org)";
    private static final String rriSymbolRegex = "(input)|(output)|(halt)|(skipcond)|(clear)|(end)";
    private static final String dataSymbolRegex = "(hex)|(dec)";

    private static final String symbolRegex = mriSymbolRegex + "|" + rriSymbolRegex + "|" + dataSymbolRegex;
    private static final String addressRegex = "([0-9][0-9a-fA-F]{0,2})|(0+[0-9a-fA-F]{0,3})";
    private static final String addressLabelRegex = "[a-zA-Z]\\w*";
    private static final String labelRegex = addressLabelRegex + ":";
    private static final String hexDataRegex = "[a-fA-F0-9]+";
    private static final String decDataRegex = "\\d+";

    private static final String[] symbols = {"jns", "load", "store", "add", "subt",
            "input", "output", "halt", "skipcond", "jump", "clear", "addi", "jumpi"};

    private String symbol;
    private String address;
    private String label;
    private String addressLabel;
    private String data;

    public Integer memoryLocation;

    public Instruction(String... instructionTokens) {
        analyze(instructionTokens);
    }

    private void analyze(String[] instructionTokens) {
        if (instructionTokens.length == 0)
            throw new InvalidInstruction("empty instruction, no tokens found");
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
                throw new InvalidInstruction("expected instruction symbol");
            if (!instructionTokens[0].toLowerCase().matches(symbolRegex))
                throw new InvalidInstruction("unknown symbol '" + instructionTokens[0] + "'");
            symbol = instructionTokens[0].toLowerCase();
        }

        private void validateTokenCountBySymbolType(String[] instructionTokens) {
            if (symbol.matches(mriSymbolRegex + "|" + dataSymbolRegex) && instructionTokens.length != 2)
                throw new InvalidInstruction("wrong token count, memory reference instruction" +
                        " should consist of one symbol and one address");
            if (symbol.matches(rriSymbolRegex) && instructionTokens.length != 1)
                throw new InvalidInstruction("wrong token count, register reference instruction" +
                        " should be only one symbol");
        }

        private void findAndSetAddress(String[] instructionTokens) {
            if (symbol.matches(mriSymbolRegex)) {
                if (instructionTokens[1].matches(addressRegex))
                    address = instructionTokens[1];
                else if (instructionTokens[1].matches(addressLabelRegex))
                    addressLabel = instructionTokens[1];
                else throw new InvalidInstruction("invalid address or address label '" + instructionTokens[1] + "'");
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
                else throw new InvalidInstruction("invalid data");
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

    public int convertToHexCode() {
        validateStatusForConversion();
        if (symbol.equals("dec")) return Integer.parseInt(data);
        if (symbol.equals("hex")) return Integer.parseInt(data, 16);
        String addressPart = (symbol.matches(rriSymbolRegex))? "000" : address;
        return Integer.parseInt(getOpcode() + addressPart, 16);
    }

        private void validateStatusForConversion() {
            if (symbol.matches(mriSymbolRegex) && address == null)
                throw new InvalidConversion();
            if (symbol.matches("(org)|(end)"))
                throw new InvalidConversion();
        }

        private String getOpcode() {
            for (int i = 0; i < symbols.length; i++) {
                if (symbols[i].equals(symbol))
                    return Integer.toHexString(i);
            }
            throw new SymbolNotFound();
        }

    public void findAddressFromTable(HashMap<String, Integer> labelAddressTable) {
        if (symbol.matches(mriSymbolRegex) && address == null) {
            Integer found = labelAddressTable.get(addressLabel);
            if (found == null) throw new LabelAddressNotFound("undefined address label '" + addressLabel + "'");
            address = Integer.toHexString(found);
        }
    }

    public boolean isEnd() {
        return symbol.equals("end");
    }

    public boolean isOrg() {
        return symbol.equals("org");
    }

    public int getAddress() {
        return Integer.parseInt(address, 16);
    }

    public boolean hasLabel() {
        return (label != null);
    }

    public String getLabel() {
        return label;
    }

    public class InvalidInstruction extends RuntimeException {
        public InvalidInstruction(String message) {
            super(message);
        }
    }

    public class SymbolNotFound extends RuntimeException {
    }

    public class InvalidConversion extends RuntimeException {
    }

    public class LabelAddressNotFound extends RuntimeException {
        public LabelAddressNotFound(String message) {
            super(message);
        }
    }

}
