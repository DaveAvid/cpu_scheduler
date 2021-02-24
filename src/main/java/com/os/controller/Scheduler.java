package com.os.controller;

import com.os.models.DaveProcess;
import com.os.models.State;

import java.util.ArrayList;
import java.util.List;


public abstract class Scheduler implements Runnable {

    protected List<DaveProcess> processList = new ArrayList<DaveProcess>();
    protected boolean IS_RUNNING = false;
    protected int runningTime = 0;
    protected DaveProcess currentProcess;

    public void addProcess(DaveProcess daveProcess) {
        daveProcess.setState(State.READY);
        processList.add(daveProcess);
    }

    public abstract DaveProcess getNextProcess();

    public void stopRunning() {
        IS_RUNNING = false;
    }
}
