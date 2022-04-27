package agents.states;

import agents.SEIRSAgent;

public class RecoveredSEIRSState extends SEIRSState {

    public RecoveredSEIRSState(SEIRSAgent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        if (agent.hasLostImmunity()) {
            agent.changeState(new SuceptibleSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return RECOVERED;
    }
}
