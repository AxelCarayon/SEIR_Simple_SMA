package scheduler;

import agents.Agent;

import java.util.*;

public class FairSynchronousScheduler extends SynchronousScheduler {

    private Agent[] agents;
    private Stack<Integer> executionOrder;

    public FairSynchronousScheduler(int seed) {
        super(seed);
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

    @Override
    public void init(Agent[] agents) {
        this.agents = agents;
        executionOrder = new Stack<>();
    }

    @Override
    public void doNextCycle() {
        generateExecutionOrder();
        wakeAgents();
    }
}
