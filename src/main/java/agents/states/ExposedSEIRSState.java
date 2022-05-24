package agents.states;

import agents.seirs.CyclicSEIRSAgent;
import agents.seirs.SEIRSAgent;

public class ExposedSEIRSState extends SEIRSState {

    public ExposedSEIRSState(SEIRSAgent agent) {
        super(agent);
    }

    @Override
    public void onMovement() {
        if (agent.isInfected()) {
            agent.changeState(new InfectedSEIRSState(agent));
        }
    }

    @Override
    public String toString() {
        return EXPOSED;
    }
}
