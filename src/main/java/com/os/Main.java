package com.os;

import com.os.algorithms.FirstComeFirstServedScheduler;
import com.os.algorithms.PriorityScheduler;
import com.os.algorithms.RoundRobinScheduler;
import com.os.algorithms.ShortJobFirstScheduler;
import com.os.controller.Scheduler;
import com.os.models.SystemProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    boolean flag = false;


    //create rate of sleep in argument of main
    public static void main(String[] args) throws FileNotFoundException {
        printSchedulerMenu();
    }

    private static void mainMenu(Scheduler scheduler) {

        int menuSelection = 0;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Select a mode for the chosen algorithm. ");
            System.out.println("1 -- Simulation Mode. " + "\n" + "2 -- Step Mode." + "\n" + "3 -- Exit." + "\n");
            System.out.print("Enter a value: ");
            menuSelection = scanner.nextInt();
            switch (menuSelection) {
                case 1:

                    printSleepSpeedMenu(scheduler);
                    break;
                case 2:
                    Scheduler scheduler1 = new PriorityScheduler();
                    printSleepSpeedMenu(scheduler1);
                    break;
                case 3:
                    System.out.println("Thanks for using this program!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Enter valid input");
                    break;
            }
        }
    }


    public static void printSchedulerMenu() {
        while (true) {
            try {
                System.out.println("____________Cpu Scheduling Simulation____________");
                System.out.println("Please Select a Scheduling Algorithm: ");
                System.out.println();
                int userChoice = printMenuTextVariableOptions("1 -- First Come First Served. " + "\n" + "2 -- Priority."
                        + "\n" + "3 -- Round Robin.\n" + "4 -- Shortest Job First\n" + "5 -- Simulation Mode\n" + "6 -- Print To File\n"
                        + "7 -- Exit", 1, 7);
                if (userChoice == 1) {
                    Scheduler scheduler = new FirstComeFirstServedScheduler();
                    mainMenu(scheduler);


                } else if (userChoice == 2) {
                    Scheduler scheduler = new PriorityScheduler();
                    mainMenu(scheduler);
                } else if (userChoice == 3) {
                    Scheduler scheduler = new RoundRobinScheduler();
                    setQuantum();
                    mainMenu(scheduler);
                } else if (userChoice == 4) {
                    Scheduler scheduler = new ShortJobFirstScheduler();
                    mainMenu(scheduler);
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

    public static void printSleepSpeedMenu(Scheduler scheduler) {
        while (true) {
            try {
                System.out.println("____________Cpu Scheduling Simulation____________");
                System.out.println("Please Select a Scheduling Algorithm: ");
                System.out.println();
                int userChoice = printMenuTextVariableOptions("1 -- 100ms " + "\n" + "2 -- 300ms "
                        + "\n" + "3 -- 500ms \n" + "7 -- Exit", 1, 7);
                if (userChoice == 1) {
                    scheduler.setSleepNumber(100);
                    readJobsFromFile("scenario.txt", scheduler);
                    startSchedulerThread(scheduler);
                } else if (userChoice == 2) {
                    scheduler.setSleepNumber(300);
                    readJobsFromFile("scenario.txt", scheduler);
                    startSchedulerThread(scheduler);
                } else if (userChoice == 3) {
                    scheduler.setSleepNumber(500);
                    readJobsFromFile("scenario.txt", scheduler);
                    startSchedulerThread(scheduler);
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

}


