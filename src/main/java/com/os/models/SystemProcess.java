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
    final private int ioWaitTime;
    final private int waitTime;
    final private int turnaroundTime;
    private int completionTime;
    private int cpuBurstRemaining = 0;
    private int ioBurstRemaining = 0;
    private State state;


    public SystemProcess(String name, int arrivalTime, int priorityLevel, int[] cpuBurst, int[] ioBurst) {
        this.name = name;
        this.pid = getNextPid();
        this.arrivalTime = arrivalTime;
        this.priorityLevel = priorityLevel;
        int totalIoTime = 0;
        int totalCpuTime = 0;
        for (int cpu : cpuBurst) {
            cpuBurstQueue.add(cpu);
            totalCpuTime += cpu;
        }
        for (int io : ioBurst) {
            ioBurstQueue.add(io);
            totalIoTime += io;
        }
        turnaroundTime = totalCpuTime + totalIoTime;
        ioWaitTime = totalIoTime;
        waitTime = totalCpuTime;
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

    public int getIoWaitTime() {
        return ioWaitTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public int getCompletionTime() {
        return completionTime;
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


    @Override
    public String toString() {
        return new org.apache.commons.lang3.builder.ToStringBuilder(this)
                .append("name", name)
                .append("pid", pid)
                .append("arrivalTime", arrivalTime)
                .append("priorityLevel", priorityLevel)
                .append("cpuBurstQueue", cpuBurstQueue)
                .append("ioBurstQueue", ioBurstQueue)
                .append("ioWaitTime", ioWaitTime)
                .append("waitTime", waitTime)
                .append("turnaroundTime", turnaroundTime)
                .append("completionTime", completionTime)
                .append("cpuBurstRemaining", cpuBurstRemaining)
                .append("ioBurstRemaining", ioBurstRemaining)
                .append("state", state)
                .toString();
    }


}
