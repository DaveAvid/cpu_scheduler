package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.DaveProcess;
import com.os.models.State;

public class FirstComeFirstServedScheduler extends Scheduler {

    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {
            currentProcess = getNextProcess();
            currentProcess.setState(State.RUNNING);
        }
    }

    public DaveProcess getNextProcess() {
        DaveProcess foundProcess = null;
        //iterate through list of processes
        for (DaveProcess process : processList) {
            //create a base case, set foundprocess to process so we have something to compare to
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
