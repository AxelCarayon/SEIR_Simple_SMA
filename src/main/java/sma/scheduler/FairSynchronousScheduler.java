package sma.scheduler;

import sma.agents.RandomWalkingAgent;

import java.util.*;

public class FairSynchronousScheduler implements Scheduler {

    private RandomWalkingAgent[] agents;
    private Stack<Integer> executionOrder;
    private Random r;

    public FairSynchronousScheduler(RandomWalkingAgent[] agents, int seed) {
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
            agents[(executionOrder.pop())].move();

        }
    }

    public void nextCycle() {
        generateExecutionOrder();
        wakeAgents();
    }
}
