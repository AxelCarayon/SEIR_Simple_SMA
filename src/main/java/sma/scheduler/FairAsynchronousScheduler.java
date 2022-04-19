package sma.scheduler;

import sma.agents.Agent;

import java.util.Arrays;
import java.util.List;
public class FairAsynchronousScheduler implements Scheduler{

    private List<Agent> agents;

    public FairAsynchronousScheduler(Agent[] agents) {
        this.agents = Arrays.asList(agents);
    }

    public void nextCycle() {
        agents.stream().parallel().forEach(a -> a.wakeUp());
    }
}
