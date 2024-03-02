package Interface;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View extends JFrame {
    public int getNumberOfQueues() {
        return Integer.parseInt(numberOfQueues.getText());
    }
    private JTextArea waitingClients;
    public int getNumberOfClients() {
        return Integer.parseInt(numberOfClients.getText());
    }
    public int getSimulationTine() {
        return Integer.parseInt(simulationTine.getText());
    }
    public void setTime(String time) {
        this.time.setText(time);
    }
    public int getMinArrivalTime() {
        return Integer.parseInt(minArrivalTime.getText());
    }
    public int getMaxArrivalTime() {
        return Integer.parseInt(maxArrivalTime.getText());
    }
    public int getMinServiceTime() {
        return Integer.parseInt(minServiceTime.getText());
    }
    public int getMaxServiceTime() {
        return Integer.parseInt(maxServiceTime.getText());
    }

    public JTable getTable1() {
        return table1;
    }
    public void setWaitingClients(StringBuilder waitingClients) {
        this.waitingClients.setText(waitingClients.toString());
    }
    public void setDimension(int w, int h) {
        add(basePanel);
        setBounds(300, 200, w, h);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    private JTextField numberOfClients;
    private JTextField simulationTine;
    private JTextField minArrivalTime;
    private JTextField maxArrivalTime;
    private JTextField minServiceTime;
    private JTextField maxServiceTime;
    private JTextField numberOfQueues;
    private JPanel basePanel;
    private JButton startButton;
    private JButton stopButton;
    private JLabel time;
    private JTable table1;
    private JLabel averageServiceTime;
    private JLabel averageWaitingTime;
    private JLabel peekHour;

    public void setAverageServiceTime(String averageServiceTime) {
        this.averageServiceTime.setText(averageServiceTime);
    }

    public void setAverageWaitingTime(String averageWaitingTime) {
        this.averageWaitingTime.setText(averageWaitingTime);
    }

    public void setPeekHour(String peekHour) {
        this.peekHour.setText(peekHour);
    }

    public View() {
        setDimension(1500,500);
        JScrollPane scrollPane = new JScrollPane(basePanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        setContentPane(scrollPane);
    }
    public void AddStartListener (ActionListener listener) {
        this.startButton.addActionListener(listener);
    }
    public void AddStopListener (ActionListener listener) {
        this.stopButton.addActionListener(listener);
    }
}
