package scheduler;

import agents.Agent;

import java.util.*;

public class FairSynchronousScheduler implements Scheduler {

    private Agent[] agents;
    private Stack<Integer> executionOrder;
    private final Random r;

    public FairSynchronousScheduler(int seed) {
        r = new Random(seed);
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
