package com.os.controller;

import com.os.models.State;
import com.os.models.SystemProcess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public abstract class Scheduler implements Runnable {

    protected Queue<SystemProcess> readyQueue = new LinkedList<SystemProcess>();
    protected boolean IS_RUNNING = false;
    protected int runningTime = 0;
    protected SystemProcess cpuCurrentProcess;
    protected SystemProcess ioCurrentProcess;
    protected Queue<SystemProcess> ioWaitQueue = new LinkedList<>();
    protected List<SystemProcess> terminatedProcessList = new ArrayList<>();

    public void addProcess(SystemProcess systemProcess) {

        systemProcess.setState(State.READY);
        //set first cpu burst to cpuburstremaining
        if (systemProcess.getCpuBurstRemaining() == 0 && !systemProcess.getCpuBurstQueue().isEmpty()) {
            systemProcess.setCpuBurstRemaining(systemProcess.getCpuBurstQueue().remove(0));
        }
        //set first io to ioburstremaining
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


    public abstract SystemProcess getNextProcess();
    //method for calculating running time from cpu burst

    public void stopRunning() {
        IS_RUNNING = false;
    }
}
