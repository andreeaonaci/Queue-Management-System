package BusinessLogic;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import Model.Task;
import Model.Server;

public class Scheduler {
    private List<Server> servers;
    public Scheduler(int maxNoServers, List<Server> servers) {
        this.servers = servers;
        for (int i = 1; i <= maxNoServers; i++) {
            Server server = new Server(i, new AtomicInteger(0));
            Thread thread = new Thread(server);
            thread.start();
            servers.add(server);
        }
    }

    public List<Server> getServers() {
        return servers;
    }
    private int sumServiceTime;
    private int sumWaitingTime;

    public int getSumServiceTime() {
        return sumServiceTime;
    }

    public int getSumWaitingTime() {
        return sumWaitingTime;
    }

    public void dispatchTask(Task task, int maxServiceTime) {
        int minWaitingTime = Integer.MAX_VALUE;
        int minWaitingTimeServer = 0;
        for (int i = 0; i < this.servers.size(); i++) {
            if (this.servers.get(i).getWaitingPeriod().get() <= minWaitingTime) {
                minWaitingTime = this.servers.get(i).getWaitingPeriod().get();
                minWaitingTimeServer = i;
            }
        }
        sumWaitingTime += minWaitingTime;
        this.servers.get(minWaitingTimeServer).addTask(task, maxServiceTime);
    }
}
