package scheduler;

import agents.Agent;

import java.security.SecureRandom;
import java.util.*;

@SuppressWarnings("ThrowablePrintedToSystemOut")
public class FairSynchronousScheduler implements Scheduler {

    private Agent[] agents;
    private Stack<Integer> executionOrder;
    private Random r;

    public FairSynchronousScheduler(Integer seed) {

        try{
            r = SecureRandom.getInstance("SHA1PRNG", "SUN");
        }catch (Exception e) {
            System.err.println(e);
            System.exit(1);
        }
        r.setSeed(seed);
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
