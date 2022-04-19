package sma.agents.states;

import sma.agents.Agent;

public class RecoveredState extends State{

    public RecoveredState(Agent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        agent.move();
    }

    @Override
    public String toString() {
        return "RECOVERED";
    }
}
