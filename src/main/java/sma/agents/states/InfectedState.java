package sma.agents.states;

import sma.agents.Agent;

public class InfectedState extends State{

    public InfectedState(Agent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        agent.move();
        if (agent.recover()) {
            agent.changeState(new RecoveredState(agent));
        }
    }

    @Override
    public String toString() {
        return "INFECTED";
    }
}
