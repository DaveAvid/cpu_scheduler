package com.os;

import com.os.algorithms.FirstComeFirstServedScheduler;
import com.os.algorithms.PriorityScheduler;
import com.os.algorithms.RoundRobinScheduler;
import com.os.algorithms.ShortJobFirstScheduler;
import com.os.controller.Scheduler;
import com.os.models.SystemProcess;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    //create rate of sleep in arguement of main
    public static void main(String[] args) throws InterruptedException {
        printSchedulerMenu();
    }


    public static void printSchedulerMenu() {
        //create one instance of scheduler
        Scheduler firstComeFirstServedScheduler = new FirstComeFirstServedScheduler();
        Scheduler shortJobFirstScheduler = new ShortJobFirstScheduler();
        Scheduler priorityScheduler = new PriorityScheduler();
        Scheduler roundRobinScheduler = new RoundRobinScheduler();
        while (true) {
            try {
                System.out.println("____________Cpu Scheduling Simulation____________");
                System.out.println("Please Select a Scheduling Algorithm: ");
                System.out.println();
                int userChoice = printMenuTextVariableOptions("1 -- First Come First Served. " + "\n" + "2 -- Priority."
                        + "\n" + "3 -- Round Robin.\n" + "4 -- Shortest Job First\n" + "5 -- Simulation Mode\n" + "6 -- Print To File\n"
                        + "7 -- Exit", 1, 7);
                if (userChoice == 1) {
                    readJobsFromFile("scenario.txt", firstComeFirstServedScheduler);
                    startSchedulerThread(firstComeFirstServedScheduler);
                } else if (userChoice == 2) {
                    readJobsFromFile("scenario.txt", priorityScheduler);
                    startSchedulerThread(priorityScheduler);
                } else if (userChoice == 3) {
                    setQuantum();
                    readJobsFromFile("scenario.txt", roundRobinScheduler);
                    startSchedulerThread(roundRobinScheduler);
                } else if (userChoice == 4) {
                    readJobsFromFile("scenario.txt", shortJobFirstScheduler);
                    startSchedulerThread(shortJobFirstScheduler);
                } else if (userChoice == 5) {
                    //Simulation Mode
                } else if (userChoice == 6) {
                    //Print to File
                } else if (userChoice == 7) {
                    System.out.println("Thanks for using this program!");
                    System.exit(0);
                    break;
                }
            } catch (Exception e) {
                System.out.println("Error has occurred.");
            }
        }
    }

    public static void printToFile() {
        //call terminated list to print to file

    }

    public static void setQuantum() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Quantum to be set: ");
        int quantumValue = scanner.nextInt();

    }


    public static int printMenuTextVariableOptions(String menuText, int min, int max) {
        Scanner console = new Scanner(System.in);
        int userInput = 0;
        while (true) {
            try {
                System.out.print(menuText + "\n");
                System.out.print("Choose a number " + min + "-" + max + ": ");
                userInput = console.nextInt();

                if (userInput < min || userInput > max) {
                    throw new Exception();
                }

                System.out.println();

                break;
            } catch (Exception e) {
                System.out.println("\n** Enter a number" + min + "-" + max + "**\n");
            }
        }
        return userInput;
    }


    private static void startSchedulerThread(Scheduler scheduler) throws InterruptedException {
        Thread thread = new Thread(scheduler);
        thread.sleep(100);
        thread.start();
    }

    public static void readJobsFromFile(String fileName, Scheduler scheduler) {
        try {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String name;
            int arrivalTime;
            int priorityLevel;
            List<Integer> cpuBurstList = new ArrayList<>();
            List<Integer> ioBurstList = new ArrayList<>();
            String line;
            //loop through all the processes in the file
            while (bufferedReader != null) {
                int i = 0;
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
                scheduler.processList.add(newProcess);
                cpuBurstList.removeAll(cpuBurstList);
                ioBurstList.removeAll(ioBurstList);
                if (bufferedReader == null) {
                    bufferedReader.close();
                }
            }
        } catch (Exception e) {
            System.out.println("File Not Found or File Done Processing.");
        }
    }

    public void writeResultsToFile(String fileName, Scheduler scheduler) {
        try {
            FileWriter writer = new FileWriter(fileName);
            PrintWriter printer = new PrintWriter(writer);

            printer.println("Process: " + scheduler.getNextProcess());
            printer.println("CPU Utilization: ");
            printer.println("Throughput: ");
            printer.println("Turn Around Time: " + scheduler);
            printer.println("Waiting Time: ");
            printer.println("Response Time: ");
            printer.close();
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}


