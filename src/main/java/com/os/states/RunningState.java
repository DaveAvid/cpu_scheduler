package com.os.states;

import com.os.controller.Scheduler;

public class RunningState extends State {
    RunningState(Scheduler scheduler) {
        super(scheduler);
    }

    @Override
    public String onNew() {
        return null;
    }
    @Override
    public String onReady() {
        return null;
    }
    @Override
    public String onRunning() {
        return null;
    }
    @Override
    public String onWaiting() {
        return null;
    }
    @Override
    public String onTerminated() {
        return null;
    }
}
