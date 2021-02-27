package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.State;
import com.os.models.SystemProcess;

public class ShortJobFirstScheduler extends Scheduler {
    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {

        }
    }

    public SystemProcess getNextProcess() {
        SystemProcess foundProcess = null;
        //iterate through list of processes
        for (SystemProcess process : processList) {
            //create a base case, set found process to process so we have something to compare to
            if (foundProcess == null) {
                foundProcess = process;
                //find the shortest priority level and perform priorty scheduling on the found process
            } else if (process.getNextCpuBurst() < foundProcess.getNextCpuBurst()) {
                foundProcess = process;
            }

        }
        return foundProcess;
    }
}
