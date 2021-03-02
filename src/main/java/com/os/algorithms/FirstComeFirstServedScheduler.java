package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.State;
import com.os.models.SystemProcess;

public class FirstComeFirstServedScheduler extends Scheduler {

    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {
            try {
                System.out.println("Current System Time: " + runningTime);
                if (cpuCurrentProcess == null) {
                    cpuCurrentProcess = getNextProcess();
                    if (cpuCurrentProcess == null) {
                        continue;
                    }

                }
                addToReadyQueue(cpuCurrentProcess);

                if (cpuCurrentProcess.cpuHasBurstRemaining() == true || !readyQueue.isEmpty()) {
                    addToCurrentCpuProcess();
                    cpuCurrentProcess.decrementCpuBurstTime();
                } else {
                    ioWaitQueue.add(cpuCurrentProcess);
                    ioCurrentProcess = ioWaitQueue.poll();
                    ioCurrentProcess.setState(State.WAITING);
                }
                //possible if/else check

                processIoBurst();
//                if (readyQueue.isEmpty()) {
//                    continue;
//                }
            } finally {
                runningTime++;

            }
        }
    }

    private void processIoBurst() {
        if (ioCurrentProcess != null) {
            ioCurrentProcess.decrementIoBurst();
            if (ioCurrentProcess.getIoBurstRemaining() == 0) {
                readyQueue.add(ioCurrentProcess);
            }
        }
    }

    public void addToReadyQueue(SystemProcess systemProcess) {
        if (systemProcess.getArrivalTime() == runningTime) {
            readyQueue.add(systemProcess);
        }
    }

    public void addToCurrentCpuProcess() {
        if (cpuCurrentProcess == null || !readyQueue.isEmpty()) {
            cpuCurrentProcess = readyQueue.poll();
            cpuCurrentProcess.setState(State.RUNNING);
        }
    }

    public SystemProcess getNextProcess() {
        SystemProcess foundProcess = null;
        //iterate through list of processes
        for (SystemProcess process : processList) {
            if (process.getArrivalTime() > runningTime) {
                continue;
            }
            //create a base case, set found process to process so we have something to compare to
            if (foundProcess == null) {
                foundProcess = process;
                //find the shortest arrival time and perform first come first served on the found process
            } else if (foundProcess.getArrivalTime() > process.getArrivalTime()) {
                foundProcess = process;
            }
        }
        return foundProcess;
    }
}
