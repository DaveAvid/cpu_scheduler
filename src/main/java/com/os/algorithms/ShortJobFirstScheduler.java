package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.State;
import com.os.models.SystemProcess;

import java.io.FileNotFoundException;

public class ShortJobFirstScheduler extends Scheduler {

    public void run() {

        IS_RUNNING = true;
        while (IS_RUNNING) {
            int threadSleep = 0;
            try {
                Thread.sleep(threadSleep);
                runningTime++;
                queueProcessesFromListOfProcesses();
                getNextProcesses();
                incrementIoQueueWaitTime();
                incrementReadyQueueWaitTime();
                if (cpuCurrentProcess == null && ioCurrentProcess == null) {
                    continue;
                }

                if (cpuCurrentProcess != null && cpuCurrentProcess.cpuHasBurstRemaining() && cpuCurrentProcess.getState() == State.RUNNING) {
                    cpuCurrentProcess.decrementCpuBurstTime();
                }
                if (ioCurrentProcess != null && ioCurrentProcess.ioHasBurstRemaining() && ioCurrentProcess.getState() == State.RUNNING) {
                    ioCurrentProcess.decrementIoBurst();
                }
                if (cpuCurrentProcess != null && cpuCurrentProcess.cpuHasBurstRemaining() == false && cpuCurrentProcess.ioHasBurstRemaining() == true) {
                    moveCurrentProcessToIoWaitQueue();
                    if (cpuCurrentProcess == null) {
                        cpuCurrentProcess = getNextProcess();

                    }

                }
                if (ioCurrentProcess != null && ioCurrentProcess.cpuHasBurstRemaining() == true && ioCurrentProcess.ioHasBurstRemaining() == false) {
                    moveCurrentProcessToReadyQueue();
                    if (ioCurrentProcess == null) {
                        ioCurrentProcess = getNextIoProcess();
                    }

                }
                getNextProcesses();


                terminateIfCpuComplete();
                terminateIfIoComplete();

                getNextProcesses();

                if (cpuCurrentProcess != null && cpuCurrentProcess.getState() == State.WAITING) {
                    cpuCurrentProcess.setState(State.RUNNING);
                }
                if (ioCurrentProcess != null && ioCurrentProcess.getState() == State.WAITING) {
                    ioCurrentProcess.setState(State.RUNNING);
                }
                printSchedulerOutput();
                if (cpuCurrentProcess != null) {
                    cpuUtilCounter++;
                }
            } catch (InterruptedException | FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getNextProcesses() {
        if (cpuCurrentProcess == null) {
            cpuCurrentProcess = getNextProcess();

        }
        if (ioCurrentProcess == null) {
            ioCurrentProcess = getNextIoProcess();
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
