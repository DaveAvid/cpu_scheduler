package com.os.controller;

import com.os.models.State;
import com.os.models.SystemProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public abstract class Scheduler implements Runnable {

    protected Queue<SystemProcess> readyQueue = new LinkedList<SystemProcess>();
    protected Queue<SystemProcess> ioWaitQueue = new LinkedList<SystemProcess>();
    protected boolean IS_RUNNING = false;
    protected int runningTime = 0;
    protected int completionTime = 0;
    protected int totalIoQueueWaitTime = 0;
    protected int totalReadyQueueWaitTime = 0;
    protected SystemProcess cpuCurrentProcess;
    protected SystemProcess ioCurrentProcess;
    protected List<SystemProcess> terminatedProcessList = new ArrayList<>();

    public void addProcess(SystemProcess systemProcess) {

        systemProcess.setState(State.READY);
        //set first cpu burst to cpuBurstRemaining
        if (systemProcess.getCpuBurstRemaining() == 0 && !systemProcess.getCpuBurstQueue().isEmpty()) {
            systemProcess.setCpuBurstRemaining(systemProcess.getCpuBurstQueue().remove(0));
        }
        //set first io to ioBurstRemaining
        if (systemProcess.getIoBurstRemaining() == 0 && !systemProcess.getIoBurstQueue().isEmpty()) {
            systemProcess.setIoBurstRemaining(systemProcess.getIoBurstQueue().remove(0));
        }
        readyQueue.add(systemProcess);
    }

    public void terminateIfCpuComplete() {
        if (cpuCurrentProcess != null && !cpuCurrentProcess.cpuHasBurstRemaining() &&
                !cpuCurrentProcess.cpuHasAdditionalBurstsInList() &&
                !cpuCurrentProcess.ioHasAdditionalBurstInList()) {
            cpuCurrentProcess.setState(State.TERMINATED);
            terminatedProcessList.add(cpuCurrentProcess);
            cpuCurrentProcess = null;
        }
    }

    public void terminateIfIoComplete() {
        if (ioCurrentProcess != null && !ioCurrentProcess.cpuHasBurstRemaining() &&
                !ioCurrentProcess.cpuHasAdditionalBurstsInList() &&
                !ioCurrentProcess.ioHasAdditionalBurstInList()) {
            ioCurrentProcess.setState(State.TERMINATED);
            terminatedProcessList.add(ioCurrentProcess);
            ioCurrentProcess = null;
        }
    }

    public void incrementReadyQueueWaitTime() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("scenario.txt"));
        int lines = 0;
        while (reader.readLine() != null) lines++;
        reader.close();
        for (SystemProcess systemProcess : readyQueue) {
            if (runningTime > systemProcess.getArrivalTime()) {
                systemProcess.incrementReadyQueueWaitTime();
                totalReadyQueueWaitTime += systemProcess.getReadyQueueWaitTime();
                totalReadyQueueWaitTime = totalReadyQueueWaitTime / lines;
            }
        }
    }


    public void incrementIoQueueWaitTime() {
        for (SystemProcess systemProcess : ioWaitQueue) {
            systemProcess.incrementIoQueueWaitTime();
            totalIoQueueWaitTime += systemProcess.getIoQueueWaitTime();
        }
    }

    public void printSchedulerOutput() throws NullPointerException{
        System.out.println();
        System.out.println("System Time: " + runningTime);

        //Print where processes are in which queues
        String printReadyQueue = "Ready Queue: ";
        if (readyQueue.isEmpty()) {
            printReadyQueue += " Empty";
        } else {
            for (SystemProcess systemProcess : readyQueue) {
                printReadyQueue += " [" + systemProcess.getName() + "]";
            }
        }
        System.out.println(printReadyQueue);
        String printIoWaitQueue = "I/O Wait Queue: ";
        if (ioWaitQueue.isEmpty()) {
            printIoWaitQueue += " Empty";
        } else {
            for (SystemProcess systemProcess : ioWaitQueue) {
                printIoWaitQueue += " [" + systemProcess.getName() + "]";
            }
        }
        //Print data to show which processes are in the CPU or the I/O
        System.out.println(printIoWaitQueue);
        if (cpuCurrentProcess != null) {
            System.out.println("CPU: [" + cpuCurrentProcess.getName() + "] " + "State: " + "[" + cpuCurrentProcess.getState() + "]");
            System.out.println("Ready Queue Wait Time: " + totalReadyQueueWaitTime);
        }
        if (ioCurrentProcess != null) {
            System.out.println("I/O: [" + ioCurrentProcess.getName() + "] " + "State: " + "[" + ioCurrentProcess.getState() + "]");
            System.out.println("Io Wait Time: " + totalIoQueueWaitTime);
        }
    }

    public void printMetrics() {
        //CPU Utilization


        //Throughput

        //Turnaround Time
        int turnAroundTime = completionTime - cpuCurrentProcess.getArrivalTime();
        //Waiting Time
        int waitingTime = turnAroundTime;
        //Response Time
    }

    public Queue<SystemProcess> getReadyQueue() {
        return readyQueue;
    }

    public abstract SystemProcess getNextProcess();
    //method for calculating running time from cpu burst

    public void stopRunning() {
        IS_RUNNING = false;
    }
}
