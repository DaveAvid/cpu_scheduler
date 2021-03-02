package com.os.controller;

import com.os.models.State;
import com.os.models.SystemProcess;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public abstract class Scheduler implements Runnable {

    protected List<SystemProcess> processList = new ArrayList<SystemProcess>();
    protected boolean IS_RUNNING = false;
    protected int runningTime = 0;
    protected SystemProcess cpuCurrentProcess;
    protected SystemProcess ioCurrentProcess;
    protected Queue<SystemProcess> readyQueue = new LinkedList<>();
    protected Queue<SystemProcess> ioWaitQueue = new LinkedList<>();
    protected List<SystemProcess> terminatedProcessList = new ArrayList<>();

    public void addProcess(SystemProcess systemProcess) {
        systemProcess.setState(State.READY);
        processList.add(systemProcess);
    }

    public void terminateProcess(SystemProcess systemProcess) {
        if (cpuCurrentProcess.cpuHasBurstRemaining() &&
                ioCurrentProcess.ioHasBurstRemaining() &&
                cpuCurrentProcess.getCpuBurstQueue().isEmpty() &&
                cpuCurrentProcess.getIoBurstQueue().isEmpty()) {
            systemProcess.setState(State.TERMINATED);
            terminatedProcessList.add(systemProcess);
        }
    }

    public abstract SystemProcess getNextProcess();
    //method for calculating running time from cpu burst

    public void stopRunning() {
        IS_RUNNING = false;
    }
}
