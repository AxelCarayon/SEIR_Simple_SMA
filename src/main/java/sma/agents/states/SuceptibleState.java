package sma.agents.states;

import sma.agents.Agent;

public class SuceptibleState extends State{

    public SuceptibleState(Agent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        if (agent.isExposed()) {
            agent.changeState(new ExposedState(agent));
        }
    }

    @Override
    public String toString() {
        return "SUCEPTIBLE";
    }
}
