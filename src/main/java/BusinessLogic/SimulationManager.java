package BusinessLogic;

import Interface.View;
import Model.Server;
import Model.Task;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SimulationManager implements Runnable {
    private View view;
    private int p;
    private int peekHour;
    public int timeLimit;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int minArrivalTime;
    public int maxArrivalTime;
    public int numberOfServers;
    private int count = 0;
    public int numberOfClients;
    private final Scheduler scheduler;
    private final List<Task> generatedTasks;

    public void setPeekHour(int peekHour) {
        this.peekHour = peekHour;
    }

    private int sumServiceTime;
    private int sumWaitingTime;
    @Override
    public void run() {
        int currentTime = 0;
        while (currentTime < this.timeLimit) {
            for (Server server : scheduler.getServers()) {
                if (server.getTasks().peek() != null) {
                    server.getTasks().peek().setServiceTime(server.getTasks().peek().getServiceTime() - 1);
                    server.getWaitingPeriod().getAndDecrement();
                    if (server.getTasks().peek() != null)
                        if (server.getTasks().peek().getServiceTime() == 0) {
                            count++;
                            sumServiceTime += server.getTasks().peek().getAuxService();
                            server.getTasks().poll();
                        }
                }
            }
            Iterator<Task> iterator = this.generatedTasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getArrivalTime() == currentTime) {
                    task.setAuxService(task.getServiceTime());
                    this.scheduler.dispatchTask(task, this.maxProcessingTime);
                    iterator.remove();
                }
            }
            view.revalidate();
            view.setTime(String.valueOf(currentTime));
            writeToFile(currentTime, this.generatedTasks);
            currentTime++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        double res;
        double res1;
        if (count > 0) {
            res = (double) sumServiceTime / count;
            this.view.setAverageServiceTime(String.valueOf(res));
        }
        else
            view.setAverageWaitingTime("No tasks finished!");
        res1 = (double) this.scheduler.getSumWaitingTime() / view.getNumberOfClients();
        this.view.setAverageWaitingTime(String.valueOf(res1));


        this.view.setPeekHour(String.valueOf(p));
    }

    private void writeToFile(int currentTime, List<Task> tasks) {
        int peek = 0;
        try {
            StringBuilder waitingClients = new StringBuilder();
            FileWriter writer = new FileWriter("output2.txt", true);
            writer.write("Time " + currentTime + "\n");
            writer.write("Waiting clients: ");

            for (Task task : tasks) {
                writer.write("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
                waitingClients.append("(").append(task.getID()).append(",").append(task.getArrivalTime()).append(",").append(task.getServiceTime()).append(")");
            }
            view.setWaitingClients(waitingClients);
            writer.write("\n");
            String[] columnNames = {"Queue", "Tasks"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);

            for (int i = 0; i < this.numberOfServers; i++) {
                String queueName = "Queue " + (i + 1);
                writer.write("Queue " + (i + 1) + ": ");
                List<String> taskStrings = new ArrayList<>();
                peek += this.scheduler.getServers().get(i).getTasks().size();
                if (this.scheduler.getServers().get(i).getTasks().peek() == null) {
                    writer.write("Closed");
                    String taskString = "Closed";
                    taskStrings.add(taskString);
                }
                for (Task task : this.scheduler.getServers().get(i).getTasks()) {
                    writer.write("(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")");
                    String taskString = "(" + task.getID() + "," + task.getArrivalTime() + "," + task.getServiceTime() + ")";
                    taskStrings.add(taskString);
                }
                writer.write(" Waiting time: " + this.scheduler.getServers().get(i).getWaitingPeriod().get());
                writer.write("\n");
                Object[] row = {queueName, String.join(",", taskStrings)};
                model.addRow(row);
            }

            JTable table = view.getTable1();
            table.setModel(model);

            if (peek > peekHour) {
                setPeekHour(peek);
                p = currentTime;
            }

            writer.write("\n");
            writer.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public SimulationManager(int numberOfClients, int numberOfServers, int timeLimit, int minArrivalTime, int maxArrivalTime, int minProcessingTime, int maxProcessingTime, View view) {
        this.view = view;
        count = 0;
        this.numberOfClients = numberOfClients;
        this.numberOfServers = numberOfServers;
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.scheduler = new Scheduler(numberOfServers, new ArrayList<>());
        this.generatedTasks = new ArrayList<>();
        for (int i = 0; i < numberOfServers; i++) {
            Thread t = new Thread(this.scheduler.getServers().get(i));
            t.start();
        }
        generateNRandomTasks(this.numberOfClients);
    }

    public void generateNRandomTasks(int n) {
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            Task task = new Task();
            task.setID(i + 1);
            task.setArrivalTime(random.nextInt(this.maxArrivalTime - this.minArrivalTime - 1) + this.minArrivalTime);
            task.setServiceTime(random.nextInt(this.maxProcessingTime - this.minProcessingTime + 1) + this.minProcessingTime);
            this.generatedTasks.add(task);
        }
        Collections.sort(this.generatedTasks);
    }
}
