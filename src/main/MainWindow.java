package main;

import javax.swing.*;

import marie.Simulator;
import net.miginfocom.swing.MigLayout;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class MainWindow {

    private JTextArea sourceCodeArea;
    private JButton resetButton;
    private JButton uploadButton;
    private JButton runButton;
    private JButton nextStepButton;
    private JTextArea consoleArea;
    private JLabel[] registerValues = new JLabel[6];
    private JTextField inputField;

    private JTable memoryTable;
    private String[][] memoryTableData;
    private JTable labelTable;
    private String[][] labelTableData;

    private Simulator simulator;

    public MainWindow() {
        createWindow();
    }

    public MainWindow(Simulator simulator) {
        this.simulator = simulator;
        createWindow();
        enableActions();
    }

    private void createWindow() {
        setLookAndFeel();
        JFrame frame = new JFrame("Marie Simulator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(750, 500);
        frame.add(createRoot());
        frame.setVisible(true);
    }

        private void setLookAndFeel() {
            try {
                String systemsLookAndFeel = UIManager.getSystemLookAndFeelClassName();
                if (!systemsLookAndFeel.equals("javax.swing.plaf.metal.MetalLookAndFeel"))
                    UIManager.setLookAndFeel(systemsLookAndFeel);
                else UIManager.setLookAndFeel(new GTKLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private JPanel createRoot() {
            JPanel root = new JPanel(new MigLayout("wrap 3", "[grow][grow][]", "[grow][]"));
            root.add(createSourceCodeAreaScrollPanel(), "span 2, wmin 300, hmin 100, grow");
            root.add(createTablePanel(), "spany, width 120:200:, grow");
            root.add(createRegisterPanel(), "grow");
            root.add(createOptionPanel(), "grow");
            return root;
        }

            private JScrollPane createSourceCodeAreaScrollPanel() {
                Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
                sourceCodeArea = new JTextArea();
                sourceCodeArea.setFont(font);
                return new JScrollPane(sourceCodeArea);
            }

            private JPanel createTablePanel() {
                JPanel tablePanel = new JPanel(new MigLayout("flowy", "[grow]", "[][grow 60][][grow 40]"));
                tablePanel.add(createBoldLabel("Memory"));
                tablePanel.add(createMemoryTable(), "grow");
                tablePanel.add(createBoldLabel("Labels"), "gaptop 10");
                tablePanel.add(createLabelTable(), "grow");
                return tablePanel;
            }

                private JLabel createBoldLabel(String text) {
                    JLabel label = new JLabel(text);
                    label.setFont(label.getFont().deriveFont(Font.BOLD));
                    return label;
                }

                private JScrollPane createMemoryTable() {
                    String[] columnNames = {"Address", "Data"};
                    memoryTable = new JTable(createMemoryTableData(), columnNames);
                    if (simulator != null) refreshMemoryTableData();
                    JScrollPane scrollPane = new JScrollPane(memoryTable);
                    scrollPane.setPreferredSize(new Dimension(150, 50));
                    return scrollPane;
                }

                    private String[][] createMemoryTableData() {
                        memoryTableData = new String[4096][2];
                        for (int i = 0; i < memoryTableData.length; i++)
                            memoryTableData[i][0] = String.format("%03X", i);
                        return memoryTableData;
                    }

                    private void refreshMemoryTableData() {
                        for (int i = 0; i < memoryTableData.length; i++)
                            memoryTableData[i][1] = String.format("%04X", simulator.getMemory().read(i) & 0xFFFF);
                        memoryTable.repaint();
                    }

                private JScrollPane createLabelTable() {
                    String[] columnNames = {"Label", "Address"};
                    labelTable = new JTable(createLabelTableData(), columnNames);
                    JScrollPane scrollPane = new JScrollPane(labelTable);
                    scrollPane.setPreferredSize(new Dimension(150, 50));
                    return scrollPane;
                }

                    private String[][] createLabelTableData() {
                        labelTableData = new String[64][2];
                        return labelTableData;
                    }

                    private void refreshLabelTableData() {
                        labelTableData = simulator.getCompiler().getLabelTableData(labelTableData);
                        labelTable.repaint();
                    }

            private JPanel createRegisterPanel() {
                JPanel registerPanel = new JPanel(new MigLayout("flowy, wrap 7"));
                addRegisterLabels(registerPanel);
                addRegisterValues(registerPanel);
                refreshRegisterValues();
                return registerPanel;
            }

                private void addRegisterLabels(JPanel registerPanel) {
                    String[] registerNames = {"AC", "MAR", "MBR", "IR", "PC", "OutReg", "InReg"};
                    for (String registerName: registerNames)
                        registerPanel.add(createBoldLabel(registerName + ":"));
                }

                private void addRegisterValues(JPanel registerPanel) {
                    inputField = new JTextField(4);
                    for (int i = 0; i < registerValues.length; i++) {
                        registerValues[i] = new JLabel("");
                        registerPanel.add(registerValues[i], "gapleft 10");
                    }
                    registerPanel.add(inputField, "gapleft 10");
                }

                private void refreshRegisterValues() {
                    int[] simulatorRegisterValues = (simulator == null)?
                            new int[7] : simulator.getMicroprocessor().getRegisterValues();
                    for (int i = 0; i < registerValues.length; i++)
                        registerValues[i].setText(String.format("%04X", simulatorRegisterValues[i] & 0xFFFF));
                    inputField.setText(String.format("%04X", simulatorRegisterValues[6] & 0xFFFF));
                }

            private JPanel createOptionPanel() {
                createButtons();
                JPanel optionPanel = new JPanel(new MigLayout("wrap 5", "[grow][][][][]", "[][grow]"));
                optionPanel.add(resetButton, "cell 1 0");
                optionPanel.add(uploadButton);
                optionPanel.add(runButton);
                optionPanel.add(nextStepButton);
                optionPanel.add(createConsoleAreaScrollPanel(), "span, width 200:300:, hmax 116, grow");
                return optionPanel;
            }

                private void createButtons() {
                    resetButton = new JButton("Reset");
                    uploadButton = new JButton("Upload");
                    runButton = new JButton("Run");
                    nextStepButton = new JButton("Next Step");
                    if (simulator != null) setButtonEnables();
                }

                    private void setButtonEnables() {
                        boolean microprocessorStopped = simulator.getMicroprocessor().isStopped();
                        runButton.setEnabled(!microprocessorStopped);
                        nextStepButton.setEnabled(!microprocessorStopped);
                    }

                private JScrollPane createConsoleAreaScrollPanel() {
                    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
                    consoleArea = new JTextArea();
                    consoleArea.setFont(font);
                    consoleArea.setEditable(false);
                    consoleArea.setLineWrap(true);
                    consoleArea.setWrapStyleWord(true);
                    return new JScrollPane(consoleArea);
                }

    private void enableActions() {
        MarieSimulatorHandler handler = new MarieSimulatorHandler();
        resetButton.addActionListener(handler);
        uploadButton.addActionListener(handler);
        runButton.addActionListener(handler);
        nextStepButton.addActionListener(handler);
    }

    private class MarieSimulatorHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            simulator.setInputDevice(inputField.getText());
            if (e.getSource().equals(resetButton))
                simulator.reset();
            else if (e.getSource().equals(uploadButton))
                simulator.uploadProgram(sourceCodeArea.getText());
            else if (e.getSource().equals(runButton))
                simulator.run();
            else if (e.getSource().equals(nextStepButton))
                simulator.runNextInstruction();
            refresh();
        }

        private void refresh() {
            consoleArea.setText(simulator.getConsoleMessage());
            refreshLabelTableData();
            refreshRegisterValues();
            refreshMemoryTableData();
            setButtonEnables();
        }

    }

}