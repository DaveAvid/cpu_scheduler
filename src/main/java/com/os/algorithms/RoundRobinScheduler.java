package com.os.algorithms;

import com.os.controller.Scheduler;
import com.os.models.DaveProcess;

public class RoundRobinScheduler extends Scheduler {
    public void run() {
        IS_RUNNING = true;
        while (IS_RUNNING) {

        }
    }

    public DaveProcess getNextProcess() {
        return null;
    }
}
