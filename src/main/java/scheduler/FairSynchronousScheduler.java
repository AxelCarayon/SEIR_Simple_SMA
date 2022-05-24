package scheduler;

import agents.Agent;
import agents.CyclicAgent;
import agents.ThreePhasedAgent;
import behaviors.Randomized;

import java.util.*;

public class FairSynchronousScheduler extends Randomized implements Scheduler {

    private Agent[] agents;
    private Stack<Integer> executionOrder;

    private Boolean isThreePhased;

    public FairSynchronousScheduler(long seed) {
        super(seed);
    }

    private void generateExecutionOrder() {
        for (int i = 0; i < agents.length; i++) {
            executionOrder.add(i);
        }
        Collections.shuffle(executionOrder,r);
    }

    private void wakeAgents(CyclicAgent[] agents) {
        while (!executionOrder.isEmpty()) {
            (agents[(executionOrder.pop())]).wakeUp();

        }
    }

    private void perceiveAll(ThreePhasedAgent[] agents) {
        for (Integer next : executionOrder) {
            agents[next].perceive();
        }
    }

    private void decideAll(ThreePhasedAgent[] agents) {
        for (Integer next : executionOrder) {
            agents[next].decide();
        }
    }

    private void actAll(ThreePhasedAgent[] agents) {
        for (Integer next : executionOrder) {
            agents[next].act();
        }
        executionOrder.clear();
    }

    @Override
    public void init(Agent[] agents) {
        isThreePhased = switch (agents[0]) {
            case CyclicAgent ignored -> false;
            case ThreePhasedAgent ignored -> true;
        };
        this.agents = agents;
        executionOrder = new Stack<>();
    }

    @Override
    public void doNextCycle() {
        generateExecutionOrder();
        if (isThreePhased) {
            ThreePhasedAgent[] agents = (ThreePhasedAgent[])this.agents;
            perceiveAll(agents);
            decideAll(agents);
            actAll(agents);
        }else {
            CyclicAgent[] agents = (CyclicAgent[])this.agents;
            wakeAgents(agents);
        }
    }
}
