package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.SystemProcess;

public class RoundRobinScheduler extends Scheduler {
    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {

        }
    }

    public SystemProcess getNextProcess() {
        //time slice
        return null;
    }
}
