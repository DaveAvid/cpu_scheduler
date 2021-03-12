package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.State;
import com.os.models.SystemProcess;

public class PriorityScheduler extends Scheduler  {
    public void run() throws NullPointerException {
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
                terminateIfCpuComplete();
                terminateIfIoComplete();
                printSchedulerOutput();

            } finally {
                runningTime++;
                completionTime = runningTime;
            }
        }
    }


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
        return ioWaitQueue.poll();
    }

    public SystemProcess getNextProcess() {
        SystemProcess foundProcess = null;
        //iterate through list of processes
        for (SystemProcess process : readyQueue) {
            if (process.getArrivalTime() > runningTime) {
                continue;
            }
            //create a base case, set found process to process so we have something to compare to
            if (foundProcess == null) {
                foundProcess = process;
                //find the shortest priority level and perform priority scheduling on the found process
            } else if (foundProcess.getPriorityLevel() > process.getPriorityLevel()) {
                foundProcess = process;
            }
        }
        if (foundProcess != null) {
            foundProcess.setState(State.RUNNING);
            readyQueue.remove(foundProcess);
        }
        return foundProcess;
    }
}
