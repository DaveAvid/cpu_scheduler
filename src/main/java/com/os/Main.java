package com.os;

import com.os.algorithms.FirstComeFirstServedScheduler;
import com.os.algorithms.PriorityScheduler;
import com.os.algorithms.ShortJobFirstScheduler;
import com.os.controller.Scheduler;
import com.os.models.SystemProcess;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //create one instance of scheduler
        Scheduler firstComeFirstServedScheduler = new FirstComeFirstServedScheduler();
        Scheduler shortJobFirstScheduler = new ShortJobFirstScheduler();
        Scheduler priorityScheduler = new PriorityScheduler();


        //create new process from file(TOODO), currently hardcoded
        {
            int[] cpuBurst = new int[]{5, 8, 9};
            int[] ioBurst = new int[]{4, 2};
            SystemProcess systemProcess = new SystemProcess("apache", 3, 2, cpuBurst, ioBurst);
            //add process to scheduler
            priorityScheduler.addProcess(systemProcess);
        }
        {
            int[] cpuBurst = new int[]{6, 7, 10};
            int[] ioBurst = new int[]{5, 3};
            SystemProcess systemProcess = new SystemProcess("french", 2, 1, cpuBurst, ioBurst);
            //add process to scheduler
            priorityScheduler.addProcess(systemProcess);
        }


        //start scheduler thread
        Thread prioritySchedulerThread = new Thread(priorityScheduler);
        prioritySchedulerThread.sleep(100);
        prioritySchedulerThread.start();

//        Thread firstComeFirstServed = new Thread(firstComeFirstServedScheduler);
//        firstComeFirstServed.sleep(100);
//        firstComeFirstServed.start();
    }


}
