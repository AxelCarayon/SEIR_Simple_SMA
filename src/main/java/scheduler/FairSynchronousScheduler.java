package scheduler;

import behaviors.Randomized;
import behaviors.Wakeable;

import java.util.*;

public class FairSynchronousScheduler extends Randomized implements Scheduler {

    private Wakeable[] agents;
    private Stack<Integer> executionOrder;

    public FairSynchronousScheduler(long seed) {
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
    public void init(Wakeable[] agents) {
        this.agents = agents;
        executionOrder = new Stack<>();
    }

    @Override
    public void doNextCycle() {
        generateExecutionOrder();
        wakeAgents();
    }
}
