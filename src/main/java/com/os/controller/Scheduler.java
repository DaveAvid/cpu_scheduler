package com.os.controller;


import com.os.CircularLinkedList;

public class Scheduler {
    private CircularLinkedList<Process> readyQueue;

    public Scheduler(){
        //constructor for class
        //when super() is called, a new circular linked list is created
        readyQueue = new CircularLinkedList<Process>();
    }
}
