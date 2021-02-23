package com.os.controller;

import com.os.states.State;
import com.os.CircularLinkedList;

public class Scheduler {
    private State state;
    private CircularLinkedList<Process> readyQueue;

    public Scheduler(){
        //constructor for class
        //when super() is called, a new circular linked list is created
        readyQueue = new CircularLinkedList<Process>();
    }
}
