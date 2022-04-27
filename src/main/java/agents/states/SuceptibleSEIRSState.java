package agents.states;

import agents.SEIRSAgent;

public class SuceptibleSEIRSState extends SEIRSState {

    public SuceptibleSEIRSState(SEIRSAgent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        if (agent.isExposed()) {
            agent.changeState(new ExposedSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return SUCEPTIBLE;
    }
}
