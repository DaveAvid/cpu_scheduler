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
    private int cpuBurstRemaining;
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

    public List<Integer> getCpuBurstQueue() {
        return cpuBurstQueue;
    }

    public List<Integer> getIoBurstQueue() {
        return ioBurstQueue;
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

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}