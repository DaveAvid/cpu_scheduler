package com.os;

import com.os.algorithms.FirstComeFirstServedScheduler;
import com.os.algorithms.PriorityScheduler;
import com.os.algorithms.ShortJobFirstScheduler;
import com.os.controller.Scheduler;
import com.os.models.SystemProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) throws InterruptedException {

        //create one instance of scheduler
        Scheduler firstComeFirstServedScheduler = new FirstComeFirstServedScheduler();
        Scheduler shortJobFirstScheduler = new ShortJobFirstScheduler();
        Scheduler scheduler = new PriorityScheduler();
        readJobsFromFile("scenario.txt", scheduler);

        //create new process from file(TOODO), currently hardcoded
//        {
//            int[] cpuBurst = new int[]{5, 8, 9};
//            int[] ioBurst = new int[]{4, 2};
//            SystemProcess systemProcess = new SystemProcess("apache", 3, 2, cpuBurst, ioBurst);
//            //add process to scheduler
//            priorityScheduler.addProcess(systemProcess);
//        }
//        {
//            int[] cpuBurst = new int[]{6, 7, 10};
//            int[] ioBurst = new int[]{5, 3};
//            SystemProcess systemProcess = new SystemProcess("french", 2, 1, cpuBurst, ioBurst);
//            //add process to scheduler
//            priorityScheduler.addProcess(systemProcess);
//        }


        //start scheduler thread
        Thread prioritySchedulerThread = new Thread(scheduler);
        prioritySchedulerThread.sleep(100);
        prioritySchedulerThread.start();

//        Thread firstComeFirstServed = new Thread(firstComeFirstServedScheduler);
//        firstComeFirstServed.sleep(100);
//        firstComeFirstServed.start();
    }

    public static void readJobsFromFile(String fileName, Scheduler scheduler) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String name;
            int arrivalTime;
            int priorityLevel;
            int counter = 0;
            List<SystemProcess> processList = new ArrayList<>();
            List<Integer> cpuBurstList = new ArrayList<>();
            List<Integer> ioBurstList = new ArrayList<>();
            String line;
            while (bufferedReader != null) {
                //loop through all the processes in the file
                for (int i = 0; i <= counter; i++) {
                    line = bufferedReader.readLine();
                    String processSplit[] = line.split(" ");
                    name = processSplit[i];
                    arrivalTime = Integer.parseInt(processSplit[i + 1]);
                    priorityLevel = Integer.parseInt(processSplit[i + 2]);

                    for (int j = 3; j < processSplit.length; j += 2) {
                        cpuBurstList.add(Integer.parseInt(processSplit[j]));

                    }
                    for (int n = 4; n < processSplit.length; n += 2) {
                        ioBurstList.add(Integer.parseInt(processSplit[n]));

                    }

                    SystemProcess newProcess = new SystemProcess(name, arrivalTime, priorityLevel, cpuBurstList, ioBurstList);
                    scheduler.addProcess(newProcess);
                    processList.add(newProcess);
                    cpuBurstList.removeAll(cpuBurstList);
                    ioBurstList.removeAll(ioBurstList);
                    counter++;
                    if (bufferedReader == null) {
                        bufferedReader.close();
                    }
                }

            }

        } catch (Exception e) {
            System.out.println("File Not Found.");
        }


    }
}


