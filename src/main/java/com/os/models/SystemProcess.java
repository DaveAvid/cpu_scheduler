package com.os.models;


import java.util.ArrayList;
import java.util.List;

public class SystemProcess {
    private static int PID_COUNTER = 0;
    final private String name;
    final private int pid;
    final private int arrivalTime;
    final private int priorityLevel;
    final private List<Integer> cpuBurstQueue = new ArrayList<Integer>();
    final private List<Integer> ioBurstQueue = new ArrayList<Integer>();
    private int turnaroundTime;
    private int ioQueueWaitTime;
    private int readyQueueWaitTime;
    private int completionTime;
    private int cpuBurstRemaining = 0;
    private int ioBurstRemaining = 0;
    private State state;


    //wait time is io burst time and how long it waits in the ready queue
//completion time - arrival time
    public SystemProcess(String name, int arrivalTime, int priorityLevel, List<Integer> cpuBurst, List<Integer> ioBurst) {
        this.name = name;
        this.pid = getNextPid();
        this.arrivalTime = arrivalTime;
        this.priorityLevel = priorityLevel;
        int totalIoTime = 0;
        int totalCpuTime = 0;
        //all the time that it is running
        //take this and subtract the total amount of time it is in my simulator
        //whatever is left is my wait time
        for (int cpu : cpuBurst) {
            cpuBurstQueue.add(cpu);
            totalCpuTime += cpu;
        }
        for (int io : ioBurst) {
            ioBurstQueue.add(io);
            totalIoTime += io;
        }
        ioQueueWaitTime = 0;
        readyQueueWaitTime = 0;
        state = State.NEW;
    }


    public static int getNextPid() {
        PID_COUNTER++;
        return PID_COUNTER;
    }

    public int getNextCpuBurst() {
        if (cpuBurstQueue.isEmpty()) {
            return 0;
        }
        return cpuBurstQueue.get(0);
    }

    public int getNextIoBurst() {
        if (ioBurstQueue.isEmpty()) {
            return 0;
        }
        return ioBurstQueue.get(0);
    }

    public List<Integer> getIoBurstQueue() {
        return ioBurstQueue;
    }

    public List<Integer> getCpuBurstQueue() {
        return cpuBurstQueue;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public int getIoQueueWaitTime() {
        return ioQueueWaitTime;
    }

    public int getReadyQueueWaitTime() {
        return readyQueueWaitTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getCpuBurstRemaining() {
        return cpuBurstRemaining;
    }

    public void setCpuBurstRemaining(int cpuBurstRemaining) {
        this.cpuBurstRemaining = cpuBurstRemaining;
    }

    public int getIoBurstRemaining() {
        return ioBurstRemaining;
    }

    public void setIoBurstRemaining(int ioBurstRemaining) {
        this.ioBurstRemaining = ioBurstRemaining;
    }

    public String getName() {
        return name;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public boolean cpuHasBurstRemaining() {
        if (cpuBurstRemaining != 0) {
            return true;
        }
        return false;
    }

    public boolean cpuHasAdditionalBurstsInList() {
        if (!cpuBurstQueue.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean ioHasAdditionalBurstInList() {
        if (!ioBurstQueue.isEmpty()) {
            return true;
        }
        return false;
    }

    public boolean ioHasBurstRemaining() {
        if (ioBurstRemaining != 0) {
            return true;
        }
        return false;
    }

    public void decrementCpuBurstTime() {
        cpuBurstRemaining--;
    }

    public void decrementIoBurst() {
        ioBurstRemaining--;
    }

    public void incrementReadyQueueWaitTime() {
        readyQueueWaitTime++;
    }

    public void incrementIoQueueWaitTime() {
        ioQueueWaitTime++;
    }

    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("name", name)
                .append("pid", pid)
                .append("arrivalTime", arrivalTime)
                .append("priorityLevel", priorityLevel)
                .append("cpuBurstQueue", cpuBurstQueue)
                .append("ioBurstQueue", ioBurstQueue)
                .append("ioWaitTime", ioQueueWaitTime)
                .append("waitTime", readyQueueWaitTime)
                .append("turnaroundTime", turnaroundTime)
                .append("completionTime", completionTime)
                .append("cpuBurstRemaining", cpuBurstRemaining)
                .append("ioBurstRemaining", ioBurstRemaining)
                .append("state", state)
                .toString();
    }


}
