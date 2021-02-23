package com.os.models;

public class Process {

    private int pid;
    private int arrivalTime;
    private int finishTime;
    private int turnaroundTime;
    private int waitTime;
    private int ioWaitTime;
    private int cpuBurstRemaining;
    private int cpuBurstTime;

    public Process(int pid, int arrivalTime, int finishTime, int turnaroundTime, int waitTime, int ioWaitTime, int cpuBurstRemaining, int cpuBurstTime) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.finishTime = finishTime;
        this.turnaroundTime = turnaroundTime;
        this.waitTime = waitTime;
        this.ioWaitTime = ioWaitTime;
        this.cpuBurstRemaining = cpuBurstRemaining;
        this.cpuBurstTime = cpuBurstTime;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(int waitTime) {
        this.waitTime = waitTime;
    }

    public int getIoWaitTime() {
        return ioWaitTime;
    }

    public void setIoWaitTime(int ioWaitTime) {
        this.ioWaitTime = ioWaitTime;
    }

    public int getCpuBurstRemaining() {
        return cpuBurstRemaining;
    }

    public void setCpuBurstRemaining(int cpuBurstRemaining) {
        this.cpuBurstRemaining = cpuBurstRemaining;
    }

    public int getCpuBurstTime() {
        return cpuBurstTime;
    }

    public void setCpuBurstTime(int cpuBurstTime) {
        this.cpuBurstTime = cpuBurstTime;
    }
}
