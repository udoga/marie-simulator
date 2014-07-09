import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.sun.java.swing.plaf.gtk.GTKLookAndFeel;

public class MainWindow {

    private JButton resetButton;
    private JButton uploadButton;
    private JButton runButton;
    private JButton runNextButton;
    private JLabel[] registerValues = new JLabel[6];

    private int simulator;

    public static void main(String[] args) {
        new MainWindow(0);
    }

    MainWindow() {
        createWindow();
    }

    MainWindow(int simulator) {
        this.simulator = simulator;
        createWindow();
        enableActionListener();
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
                UIManager.setLookAndFeel(new GTKLookAndFeel());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private JPanel createRoot() {
            JPanel root = new JPanel(new MigLayout("wrap 3", "[grow][][]", "[grow][]"));
            root.add(createTextAreaScrollPanel(), "span 2, wmin 300, hmin 100, grow");
            root.add(createTablePanel(), "spany, width 120:200:, grow");
            root.add(createRegisterPanel(), "grow");
            root.add(createOptionPanel(), "grow");
            return root;
        }

            private JScrollPane createTextAreaScrollPanel() {
                Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
                JTextArea textArea = new JTextArea();
                textArea.setFont(font);
                return new JScrollPane(textArea);
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
                    String[][] memoryTableData = new String[4096][2];
                    JTable memoryTable = new JTable(memoryTableData, columnNames);
                    JScrollPane scrollPane = new JScrollPane(memoryTable);
                    scrollPane.setPreferredSize(new Dimension(150, 50));
                    return scrollPane;
                }

                private JScrollPane createLabelTable() {
                    String[] columnNames = {"Label", "Address"};
                    String[][] labelTableData = new String[64][2];
                    JTable labelTable = new JTable(labelTableData, columnNames);
                    JScrollPane scrollPane = new JScrollPane(labelTable);
                    scrollPane.setPreferredSize(new Dimension(150, 50));
                    return scrollPane;
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
                    for (int i = 0; i < registerValues.length; i++) {
                        registerValues[i] = new JLabel("");
                        registerPanel.add(registerValues[i], "gapleft 10");
                    }
                    registerPanel.add(new JTextField(4), "gapleft 10");
                }

                private void refreshRegisterValues() {
                    for (JLabel registerValue : registerValues)
                        registerValue.setText("0000");
                }

            private JPanel createOptionPanel() {
                JPanel optionPanel = new JPanel(new MigLayout());
                createButtons();
                optionPanel.add(resetButton);
                optionPanel.add(uploadButton);
                optionPanel.add(runButton, "wrap");
                optionPanel.add(runNextButton, "spanx, right, gaptop 10");
                return optionPanel;
            }

                private void createButtons() {
                    resetButton = new JButton("Reset");
                    uploadButton = new JButton("Upload Program");
                    runButton = new JButton("Run Program");
                    runNextButton = new JButton("Run Next Step");
                }

    private void enableActionListener() {
        MarieSimulatorHandler handler = new MarieSimulatorHandler();
        resetButton.addActionListener(handler);
        uploadButton.addActionListener(handler);
        runButton.addActionListener(handler);
        runNextButton.addActionListener(handler);
    }

    private class MarieSimulatorHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(resetButton))
                System.out.println("reset");
            else if (e.getSource().equals(uploadButton))
                System.out.println("upload");
            else if (e.getSource().equals(runButton))
                System.out.println("run");
            else if (e.getSource().equals(runNextButton))
                System.out.println("run next");
        }

    }

}