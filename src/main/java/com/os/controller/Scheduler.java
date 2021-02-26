package com.os.controller;

import com.os.models.State;
import com.os.models.SystemProcess;

import java.util.ArrayList;
import java.util.List;


public abstract class Scheduler implements Runnable {

    protected List<SystemProcess> processList = new ArrayList<SystemProcess>();
    protected boolean IS_RUNNING = false;
    protected int runningTime = 0;
    protected SystemProcess currentProcess;

    public void addProcess(SystemProcess systemProcess) {
        systemProcess.setState(State.READY);
        processList.add(systemProcess);
    }

    public abstract SystemProcess getNextProcess();

    public void stopRunning() {
        IS_RUNNING = false;
    }
}
