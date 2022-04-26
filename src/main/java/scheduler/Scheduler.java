package scheduler;

import agents.Agent;

public interface Scheduler {

    void init(Agent[] agents);
    void doNextCycle();

}
