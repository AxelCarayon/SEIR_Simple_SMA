package sma.scheduler;

import sma.agents.Agent;

import java.util.*;

public class FairSynchronousScheduler implements Scheduler {

    private Agent[] agents;
    private Stack<Integer> executionOrder;
    private Random r;

    public FairSynchronousScheduler(Agent[] agents, int seed) {
        this.agents = agents;
        r = new Random(seed);
        executionOrder = new Stack<>();
    }

    private void generateExecutionOrder() {
        for (int i = 0; i < agents.length; i++) {
            executionOrder.add(i);
        }
        Collections.shuffle(executionOrder,r);
    }

    private void wakeAgents() {
        while (!executionOrder.isEmpty()) {
            agents[(executionOrder.pop())].wakeUp();

        }
    }

    public void nextCycle() {
        generateExecutionOrder();
        wakeAgents();
    }
}
