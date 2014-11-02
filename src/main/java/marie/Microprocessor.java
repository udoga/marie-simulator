package marie;

public class Microprocessor {

    private Memory memory;

    private int MAR;
    private int PC;
    private int MBR;
    private int AC;
    private int IR;
    private int InREG;
    private int OutREG;

    private int opcode;
    private boolean stopped = true;

    public Microprocessor(Memory memory) {
        this.memory = memory;
    }

    public void start(int value) {
        stopped = false;
        PC = value;
    }

    public void run() {
        while (!stopped)
            runNextInstruction();
    }

    public void runNextInstruction() {
        fetch();
        decode();
        execute();
    }

    private void fetch() {
        MAR = PC;
        IR = memory.read(MAR);
        PC = PC + 1;
    }

    private void decode() {
        MAR = Integer.parseInt(String.format("%04x", IR).substring(1), 16);
        opcode = Integer.parseInt(String.format("%04x", IR).substring(0, 1), 16);
        MBR = memory.read(MAR);
    }

    private void execute() {
        switch (opcode) {
            case 0x0: jns(); break;
            case 0x1: load(); break;
            case 0x2: store(); break;
            case 0x3: add(); break;
            case 0x4: subt(); break;
            case 0x5: input(); break;
            case 0x6: output(); break;
            case 0x7: halt(); break;
            case 0x8: skipcond(); break;
            case 0x9: jump(); break;
            case 0xA: clear(); break;
            case 0xB: addi(); break;
            case 0xC: jumpi(); break;
            case 0xD: loadi(); break;
            case 0xE: storei(); break;
        }
    }

    private void jns() {
        MBR = PC;
        memory.write(MAR, MBR);
        MBR = MAR;
        AC = 1;
        AC = AC + MBR;
        PC = AC;
    }

    private void load() {
        AC = MBR;
    }

    private void store() {
        MBR = AC;
        memory.write(MAR, MBR);
    }

    private void add() {
        AC = AC + MBR;
    }

    private void subt() {
        AC = AC - MBR;
    }

    private void input() {
        AC = InREG;
    }

    private void output() {
        OutREG = AC;
    }

    private void halt() {
        stopped = true;
    }

    private void skipcond() {
        String IRpart = String.format("%16s", Integer.toBinaryString(MAR)).replace(' ', '0').substring(4, 6);
        if ((IRpart.equals("00") && AC < 0) || (IRpart.equals("01") && AC == 0) || (IRpart.equals("10") && AC > 0))
            PC = PC + 1;
    }

    private void jump() {
        PC = MAR;
    }

    private void clear() {
        AC = 0;
    }

    private void addi() {
        MAR = MBR;
        MBR = memory.read(MAR);
        AC = AC + MBR;
    }

    private void jumpi() {
        PC = MBR;
    }

    private void loadi() {
        MAR = MBR;
        MBR = memory.read(MAR);
        AC = AC + MBR;
    }

    private void storei() {
        MAR = MBR;
        memory.write(MAR, AC);
    }

    public int[] getRegisterValues() {
        return new int[]{AC, MAR, MBR, IR, PC, OutREG, InREG};
    }

    public void reset() {
        AC = 0; MAR = 0; MBR = 0; IR = 0; PC = 0; OutREG = 0; InREG = 0;
        stopped = true;
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setInput(int value) {
        InREG = value;
    }

}
