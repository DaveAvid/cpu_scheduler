package com.os.models;

import com.os.List;
import com.os.states.State;

import java.util.ArrayList;

public class Process {
    final private String name;
    final private int pid;
    final private int arrivalTime;
    final private int priorityLevel;
    final private ArrayList<Integer> cpuBurstTime = new ArrayList<Integer>();
    final private ArrayList<Integer> ioBurstTime = new ArrayList<Integer>();
    private int completionTime;
    private int turnaroundTime;
    private int waitTime;
    final private int ioWaitTime;
    private int cpuBurstRemaining;
    private State state;



}
