package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.State;
import com.os.models.SystemProcess;

public class ShortJobFirstScheduler extends Scheduler {

    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {

            try {
                if (cpuCurrentProcess == null) {
                    cpuCurrentProcess = getNextProcess();

                }
                if (ioCurrentProcess == null) {
                    ioCurrentProcess = getNextIoProcess();
                }
                if (cpuCurrentProcess == null && ioCurrentProcess == null) {
                    continue;
                }
                if (cpuCurrentProcess != null && cpuCurrentProcess.cpuHasBurstRemaining()) {
                    cpuCurrentProcess.decrementCpuBurstTime();
                }
                if (ioCurrentProcess != null && ioCurrentProcess.ioHasBurstRemaining()) {
                    ioCurrentProcess.decrementIoBurst();
                }
                if (cpuCurrentProcess != null && !cpuCurrentProcess.cpuHasBurstRemaining() && cpuCurrentProcess.ioHasBurstRemaining()) {
                    moveCurrentProcessToIoWaitQueue();

                }
                if (ioCurrentProcess != null && ioCurrentProcess.cpuHasBurstRemaining() && !ioCurrentProcess.ioHasBurstRemaining()) {
                    moveCurrentProcessToReadyQueue();

                }
                printSchedulerOutput();
                terminateIfCpuComplete();
                terminateIfIoComplete();

            } finally {
                runningTime++;
                completionTime = runningTime;
            }
        }
    }
//add more processes
    //confirm by hand

    private void moveCurrentProcessToIoWaitQueue() {
        //set first cpu burst to cpuburstremaining
        if (cpuCurrentProcess.getCpuBurstRemaining() == 0 && !cpuCurrentProcess.getCpuBurstQueue().isEmpty()) {
            cpuCurrentProcess.setCpuBurstRemaining(cpuCurrentProcess.getCpuBurstQueue().remove(0));
        }
        ioWaitQueue.add(cpuCurrentProcess);
        cpuCurrentProcess = null;
    }

    private void moveCurrentProcessToReadyQueue() {
        //set first io to ioburstremaining
        if (ioCurrentProcess.getIoBurstRemaining() == 0 && !ioCurrentProcess.getIoBurstQueue().isEmpty()) {
            ioCurrentProcess.setIoBurstRemaining(ioCurrentProcess.getIoBurstQueue().remove(0));
        }
        readyQueue.add(ioCurrentProcess);
        ioCurrentProcess = null;
    }

    private SystemProcess getNextIoProcess() {
        SystemProcess foundProcess = ioWaitQueue.poll();
        return foundProcess;
    }

    public SystemProcess getNextProcess() {
        SystemProcess foundProcess = null;
        //iterate through queue of processes
        for (SystemProcess process : readyQueue) {
            //create a base case, set found process to process so we have something to compare to
            if (foundProcess == null) {
                foundProcess = process;
                //find the shortest priority level and perform priorty scheduling on the found process
            } else if (process.getNextCpuBurst() > foundProcess.getNextCpuBurst()) {
                foundProcess = process;
            }
        }
        if (foundProcess != null) {
            foundProcess.setState(State.RUNNING);
            //now that we found next process, remove from ready queue.
            readyQueue.remove(foundProcess);
        }

        return foundProcess;
    }
}
