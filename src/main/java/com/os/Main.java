package com.os;

import com.os.algorithms.FirstComeFirstServedScheduler;
import com.os.controller.Scheduler;
import com.os.models.SystemProcess;

public class Main {
    public static void main(String[] args) {
        //create one instance of scheduler
        Scheduler scheduler = new FirstComeFirstServedScheduler();

        //create new process from file(TOODO), currently hardcoded
        {
            int[] cpuBurst = new int[]{5, 8, 9};
            int[] ioBurst = new int[]{4, 2};
            SystemProcess systemProcess = new SystemProcess("apache", 3, 2, cpuBurst, ioBurst);
            //add process to scheduler
            scheduler.addProcess(systemProcess);
        }
        {
            int[] cpuBurst = new int[]{5, 8, 9};
            int[] ioBurst = new int[]{4, 2};
            SystemProcess systemProcess = new SystemProcess("french", 2, 1, cpuBurst, ioBurst);
            //add process to scheduler
            scheduler.addProcess(systemProcess);
        }

        //start scheduler thread
        Thread schedulerThread = new Thread(scheduler);
        schedulerThread.start();

    }
}
