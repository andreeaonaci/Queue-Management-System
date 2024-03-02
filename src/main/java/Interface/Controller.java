package Interface;

import BusinessLogic.SimulationManager;
import Model.Task;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.List;

public class Controller {
    private View view;
    public Controller(View view) {
        this.view = view;
        view.AddStartListener(new startButton());
        view.AddStopListener(new stopButton());
    }
    static class stopButton implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            System.exit(0);
        }
    }

    class startButton implements ActionListener {
        public void actionPerformed(ActionEvent arg0) {
            try {
                int numberOfClients = view.getNumberOfClients();
                int numberOfQueues = view.getNumberOfQueues();
                int simulationTime = view.getSimulationTine();
                int minArrivalTime = view.getMinArrivalTime();
                int maxArrivalTime = view.getMaxArrivalTime();
                int minServiceTime = view.getMinServiceTime();
                int maxServiceTime = view.getMaxServiceTime();
                if (numberOfClients > 0 && numberOfQueues > 0 && simulationTime > 0 && minArrivalTime > 0 && maxArrivalTime > minArrivalTime && minServiceTime > 0 && maxServiceTime > minServiceTime) {
                    SimulationManager simulationManager = new SimulationManager(numberOfClients, numberOfQueues, simulationTime, minArrivalTime, maxArrivalTime, minServiceTime, maxServiceTime, view);
                    Thread thread = new Thread(simulationManager);
                    thread.start();
                } else
                    JOptionPane.showMessageDialog(view, "The input is not correct!");
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(view, "The input is null/invalid!");
            }
        }
    }
}
