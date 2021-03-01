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
                if (currentProcess == null) {
                    currentProcess = getNextProcess();
                    if (currentProcess == null) {
                        continue;
                    }
                    currentProcess.setState(State.RUNNING);
                }
                if (currentProcess != null) {
                    addToReadyQueue();
                    currentProcess.decrementCpuBurstTime();
                }
                if (!readyQueue.isEmpty() && currentProcess.getCpuBurstRemaining() == 0) {
                    ioWaitQueue.add(currentProcess);
                    ioCurrentProcess = ioWaitQueue.poll();
                    ioCurrentProcess.setState(State.WAITING);
                }
                if (ioCurrentProcess != null) {
                    ioCurrentProcess.decrementIoBurst();
                    if (ioCurrentProcess.getIoBurstRemaining() == 0) {
                        readyQueue.add(ioCurrentProcess);
                    }
                }

                if (readyQueue.isEmpty()) {
                    continue;
                }
            } finally {
                runningTime++;

            }
        }
    }

    public void addToReadyQueue() {
        for (SystemProcess foundProcess : processList) {
            if (foundProcess.getArrivalTime() == runningTime) {
                readyQueue.add(foundProcess);
            }
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
