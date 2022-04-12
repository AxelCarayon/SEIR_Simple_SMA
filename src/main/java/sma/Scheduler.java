package sma;

import view.GraphicEnvironment;

import java.util.*;

public class Scheduler{

    private Agent[] agents;
    private GraphicEnvironment gEnvironment;
    private Stack<Integer> executionOrder;
    private Random r;

    public Scheduler(Agent[] agents,int seed) {
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

    public void nextCycle() throws InterruptedException {
        generateExecutionOrder();
        wakeAgents();
    }
}
