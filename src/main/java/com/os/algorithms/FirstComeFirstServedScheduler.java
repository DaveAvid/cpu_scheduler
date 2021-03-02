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
                addToReadyQueue();

                if (cpuCurrentProcess.cpuHasBurstRemaining()) {
                    addToCurrentCpuProcess();
                    cpuCurrentProcess.decrementCpuBurstTime();
                } else {
                    addToCurrentIoProcess();
                }
                //possible if/else check
                processIoBurst();
            } finally {
                runningTime++;


            }
        }
    }

    private void addToCurrentIoProcess() {
        if (cpuCurrentProcess.cpuHasBurstRemaining()) {

        } else {
            ioWaitQueue.add(cpuCurrentProcess);
            if (ioCurrentProcess == null) {
                ioCurrentProcess = ioWaitQueue.poll();
                ioCurrentProcess.setState(State.WAITING);
            }
        }
    }

    public void addToCurrentCpuProcess() {
        if (cpuCurrentProcess.getCpuBurstRemaining() == 0) {
            cpuCurrentProcess = readyQueue.poll();
            cpuCurrentProcess.setState(State.RUNNING);
        }
    }

    private void processIoBurst() {
        if (ioCurrentProcess != null) {
            ioCurrentProcess.decrementIoBurst();
            if (ioCurrentProcess.ioHasBurstRemaining()) {

            } else {
                readyQueue.add(ioCurrentProcess);
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
