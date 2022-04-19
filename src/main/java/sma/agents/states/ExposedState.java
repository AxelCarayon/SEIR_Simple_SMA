package sma.agents.states;

import sma.agents.Agent;

public class ExposedState extends State{

    public ExposedState(Agent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        if (agent.isInfected()) {
            agent.changeState(new InfectedState(agent));
        }
    }

    @Override
    public String toString() {
        return "EXPOSED";
    }
}
