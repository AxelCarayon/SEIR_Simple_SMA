package sma;

import agents.Agent;
import environment.Environment;
import scheduler.Scheduler;

public interface SMA{

    void init(Environment environment, Scheduler scheduler, Agent[] agents);
    void run();
}
