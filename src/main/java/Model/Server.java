package Model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingQueue<Task> tasks;
    public AtomicInteger getWaitingPeriod() {
        return this.waitingPeriod;
    }
    private AtomicInteger waitingPeriod;
    private final int index;
    public Server(int index, AtomicInteger waitingPeriod) {
        this.tasks = new ArrayBlockingQueue<>(100);
        this.index = index;
        this.waitingPeriod = waitingPeriod;
    }

    public void addTask(Task newTask, int maxServiceTime) {
        synchronized (this.tasks) {
            this.tasks.add(newTask);
        }
        int serviceTime = Math.min(newTask.getServiceTime(), maxServiceTime);
        int waitingTime = this.waitingPeriod.get();
        synchronized (this.waitingPeriod) {
            this.waitingPeriod.getAndAdd(serviceTime);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted() || !tasks.isEmpty()) {
            if (!tasks.isEmpty()) {
                Task t = tasks.peek();
                if (t != null && t.getServiceTime() == 0) {
                    tasks.poll();
                }
                if (t != null && Math.abs(t.getServiceTime() - this.waitingPeriod.get()) > 0) {
                    try {
                        Thread.sleep(t.getServiceTime() * 100L);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public BlockingQueue<Task> getTasks() {
        return this.tasks;
    }
}
