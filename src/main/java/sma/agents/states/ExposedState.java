package sma.agents.states;

import sma.agents.Agent;

public class ExposedState extends State{

    public ExposedState(Agent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        agent.move();
        if (agent.incubate()) {
            agent.changeState(new InfectedState(agent));
        }
    }

    @Override
    public String toString() {
        return "EXPOSED";
    }
}
