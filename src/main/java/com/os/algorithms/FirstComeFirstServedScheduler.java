package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.DaveProcess;

public class FirstComeFirstServed extends Scheduler {

    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {
            currentProcess = getNextProcess();

        }
    }

    private DaveProcess getNextProcess() {
        DaveProcess foundProcess = null;
        for (DaveProcess process : processList) {
            if (foundProcess == null) {
                foundProcess = process;
            } else if (foundProcess.getArrivalTime() > process.getArrivalTime()) {
                foundProcess = process;
            }
        }
    }
}
