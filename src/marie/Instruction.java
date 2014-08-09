package marie;

public class Instruction {

    private String symbol;
    private String address;

    private String label;
    private String addressLabel;

    public Instruction(String symbol) {
        this.symbol = symbol;
    }

    public Instruction(String symbol, String address) {
        this.symbol = symbol;
        this.address = address;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setAddressLabel(String addressLabel) {
        this.addressLabel = addressLabel;
    }

    public String toString() {
        String labelInfo = (label == null)? "" : (", Label: " + label);
        String symbolInfo = (symbol == null)? "" : (", Symbol: " + symbol);
        String addressInfo = (address == null)? "" : (", Address: " + address);
        String addressLabelInfo = (addressLabel == null)? "" : (", Address Label: " + addressLabel);

        String info = (labelInfo + symbolInfo + addressInfo + addressLabelInfo).substring(2);
        return "Instruction(" + info + ")";
    }

}
