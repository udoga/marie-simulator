package main;

import gui.MainWindow;
import marie.Simulator;

public class MarieSimulatorApp {

    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        new MainWindow(simulator);
    }

}
