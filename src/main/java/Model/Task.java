package Model;

public class Task implements Comparable<Task> {
    private int ID;
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public int getArrivalTime() {
        return arrivalTime;
    }
    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
    public int getServiceTime() {
        return serviceTime;
    }

    public int getAuxService() {
        return auxService;
    }

    public void setAuxService(int auxService) {
        this.auxService = auxService;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }
    private int arrivalTime;
    private int serviceTime;
    private int auxService;

    @Override
    public int compareTo(Task task) {
        return Integer.compare(this.arrivalTime, task.arrivalTime);
    }
}
