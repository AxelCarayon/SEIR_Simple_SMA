package agents.states;

import agents.SEIRSAgent;

public class InfectedSEIRSState extends SEIRSState {

    public InfectedSEIRSState(SEIRSAgent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        if (agent.isRecovered()) {
            agent.changeState(new RecoveredSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return INFECTED;
    }
}
