package com.os.states;

import com.os.controller.Scheduler;

public abstract class State {
    Scheduler scheduler;

    State(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public abstract String onNew();
    public abstract String onReady();
    public abstract String onRunning();
    public abstract String onWaiting();
    public abstract String onTerminated();
}
