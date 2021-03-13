package com.os.controller;

import com.os.models.State;
import com.os.models.SystemProcess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public abstract class Scheduler implements Runnable {

    public List<SystemProcess> processList = new ArrayList<>();
    public List<Integer> waitTimesList = new ArrayList<>();
    public List<Integer> turnaroundTimes = new ArrayList<>();
    protected Queue<SystemProcess> readyQueue = new LinkedList<>();
    protected Queue<SystemProcess> ioWaitQueue = new LinkedList<>();
    protected boolean IS_RUNNING = false;
    protected int runningTime = -1;
    protected int completionTime = 0;
    protected int totalTimeInWaitQueues = 0;
    protected double averageTurnaroundTime = 0;
    protected int averageWaitTime = 0;
    protected double cpuUtilCounter = 0.0;
    protected SystemProcess cpuCurrentProcess;
    protected SystemProcess ioCurrentProcess;
    protected List<SystemProcess> terminatedProcessList = new ArrayList<>();

    private void terminationPrint(SystemProcess systemProcess) {
        System.out.println();
        System.out.println("| Process Termination Detected |");
        System.out.println("Terminated Process " + systemProcess.getName() + " at " + systemProcess.getCompletionTime() + " " + " [" + systemProcess.getState() + "]");
        System.out.println("Total Turnaround Time " + systemProcess.getTurnaroundTime());
        System.out.println("Total Cpu Wait Time " + systemProcess.getReadyQueueWaitTime());
        System.out.println("Total Io Wait Time " + systemProcess.getIoQueueWaitTime());
        System.out.println("Cpu Utilization " + ((cpuUtilCounter / runningTime) * 100) + "% ");
        System.out.println("Average Turnaround Time: " + calculateAverageTurnaroundTime());
        System.out.println("Average Wait Time: " + calculateAverageWaitTime());

    }

    public void addTurnaroundTimesToList(SystemProcess systemProcess) {
        turnaroundTimes.add(systemProcess.getTurnaroundTime());
    }

    public double calculateAverageTurnaroundTime() {
        int totalTurnaroundTimes = 0;
        int numberOfTurnaroundTimes = turnaroundTimes.size();
        for (Integer turnAroundTime : turnaroundTimes) {
            totalTurnaroundTimes += turnAroundTime;
        }
        averageTurnaroundTime = (totalTurnaroundTimes / numberOfTurnaroundTimes);

        return averageTurnaroundTime;
    }

    public double calculateAverageWaitTime() {
        int totalWaitTimes = 0;
        int numberOfWaitTimes = waitTimesList.size();
        for (Integer waitTime : waitTimesList) {
            totalWaitTimes += waitTime;
        }
        averageWaitTime = (totalWaitTimes / numberOfWaitTimes);
        return averageWaitTime;
    }

    public void addWaitTimesToList(SystemProcess systemProcess) {
        totalTimeInWaitQueues += systemProcess.getReadyQueueWaitTime() + systemProcess.getIoQueueWaitTime();
        waitTimesList.add(totalTimeInWaitQueues);
        totalTimeInWaitQueues = 0;
    }

    public void terminateIfCpuComplete() {
        if (cpuCurrentProcess != null && !cpuCurrentProcess.cpuHasBurstRemaining() &&
                !cpuCurrentProcess.cpuHasAdditionalBurstsInList() &&
                !cpuCurrentProcess.ioHasAdditionalBurstInList()) {
            cpuCurrentProcess.setState(State.TERMINATED);
            cpuCurrentProcess.setCompletionTime(runningTime);
            cpuCurrentProcess.setTurnaroundTime(cpuCurrentProcess.getCompletionTime() - cpuCurrentProcess.getArrivalTime());
            addWaitTimesToList(cpuCurrentProcess);
            addTurnaroundTimesToList(cpuCurrentProcess);
            terminationPrint(cpuCurrentProcess);
            terminatedProcessList.add(cpuCurrentProcess);
            cpuCurrentProcess = null;
        }
    }

    public void terminateIfIoComplete() {
        if (ioCurrentProcess != null && !ioCurrentProcess.cpuHasBurstRemaining() &&
                !ioCurrentProcess.cpuHasAdditionalBurstsInList() &&
                !ioCurrentProcess.ioHasAdditionalBurstInList()) {
            ioCurrentProcess.setState(State.TERMINATED);
            ioCurrentProcess.setCompletionTime(runningTime);
            ioCurrentProcess.setTurnaroundTime(ioCurrentProcess.getCompletionTime() - ioCurrentProcess.getArrivalTime());
            terminationPrint(ioCurrentProcess);
            terminatedProcessList.add(ioCurrentProcess);
            ioCurrentProcess = null;
        }
    }

    public void incrementReadyQueueWaitTime() {
        for (SystemProcess systemProcess : readyQueue) {
            if (runningTime > systemProcess.getArrivalTime()) {
                systemProcess.incrementReadyQueueWaitTime();
            }
        }
    }


    public void incrementIoQueueWaitTime() {
        for (SystemProcess systemProcess : ioWaitQueue) {
            systemProcess.incrementIoQueueWaitTime();
        }
    }

    public void printSchedulerOutput() throws NullPointerException {
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

        System.out.println("CPU: [" + (cpuCurrentProcess != null ? cpuCurrentProcess.getName() : "") + "] " + "State: " + "[" + (cpuCurrentProcess != null ? cpuCurrentProcess.getState() : "") + "]");


        System.out.println("I/O: [" + (ioCurrentProcess != null ? ioCurrentProcess.getName() : "") + "] " + "State: " + "[" + (ioCurrentProcess != null ? ioCurrentProcess.getState() : "") + "]");


    }

    public Queue<SystemProcess> getReadyQueue() {
        return readyQueue;
    }

    protected void queueProcessesFromListOfProcesses() {
        if (!processList.isEmpty()) {
            List<SystemProcess> processesToBeRemoved = new ArrayList<>();
            for (SystemProcess systemProcess : processList) {
                if (systemProcess.getArrivalTime() == runningTime) {
                    systemProcess.setState(State.WAITING);
                    //set first cpu burst to cpuBurstRemaining
                    if (systemProcess.getCpuBurstRemaining() == 0 && !systemProcess.getCpuBurstQueue().isEmpty()) {
                        systemProcess.setCpuBurstRemaining(systemProcess.getCpuBurstQueue().remove(0));
                    }
                    //set first io to ioBurstRemaining
                    if (systemProcess.getIoBurstRemaining() == 0 && !systemProcess.getIoBurstQueue().isEmpty()) {
                        systemProcess.setIoBurstRemaining(systemProcess.getIoBurstQueue().remove(0));
                    }
                    readyQueue.add(systemProcess);

                    processesToBeRemoved.add(systemProcess);
                }

            }
            if (!processesToBeRemoved.isEmpty()) {
                for (SystemProcess systemProcess : processesToBeRemoved) {
                    processList.remove(systemProcess);
                }
                processesToBeRemoved.clear();
            }
        }
    }

    public abstract SystemProcess getNextProcess();
    //method for calculating running time from cpu burst

    public void stopRunning() {
        IS_RUNNING = false;
    }
}
